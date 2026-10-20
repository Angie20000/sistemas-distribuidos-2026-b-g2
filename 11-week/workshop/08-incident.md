# CP9 · Incident drill — Booking slow on `expirar`

## Setup of the drill

Marked hop of CP1: hop 3, `property-worker` → `booking-service`,
`POST /api/v1/reservas/{id}/expirar`. For **40 minutes**, Booking answers in **3 s for
25 %** of the calls. The worker gives up on a call at 3 s and retries it (up to 3
attempts, CP2), so a 3 s answer counts as a timeout on the worker side.

To have something to expire, the drill starts just after a short Stripe outage that left
**16 reservations** in `PENDIENTE`. The job finds them in its first runs.

## One slow call inside one run

Run of 14:12 — `trace_id = 1c7e4a9b2d3f4e5a8b6c7d8e9f0a1b2c`

```
job expire-stale-reservations run ............................ property-worker   7,904 ms
├── GET /api/v1/reservas?estado=PENDIENTE&creadaAntesDe=... ... booking-service     38 ms
├── POST /api/v1/reservas/{id}/expirar  (attempt 1) .......... booking-service  3,000 ms  timeout
├── wait before retry ........................................ property-worker   1,000 ms
├── POST /api/v1/reservas/{id}/expirar  (attempt 2) .......... booking-service     44 ms
│   ├── UPDATE booking.reservation -> EXPIRADA ............... booking-service      6 ms
│   └── INSERT booking.outbox_event (traceparent) ............ booking-service     11 ms
└── POST /api/v1/reservas/{id}/expirar  (next reservation) ... booking-service  3,012 ms  slow, answered
relay publish ReservaExpirada ................................ booking-service     15 ms  (async)
```

## Four lines of that trace

```json
{"timestamp":"2026-10-24T14:12:00.004Z","level":"INFO","service":"property-worker","version":"2.0.0","environment":"main","trace_id":"1c7e4a9b2d3f4e5a8b6c7d8e9f0a1b2c","span_id":"aa11bb22cc33dd44","correlation_id":"run-20261024-1412","event":"job.run.started","message":"expire-stale-reservations run started","job":"expire-stale-reservations","batch_size":50}
{"timestamp":"2026-10-24T14:12:03.046Z","level":"ERROR","service":"property-worker","version":"2.0.0","environment":"main","trace_id":"1c7e4a9b2d3f4e5a8b6c7d8e9f0a1b2c","span_id":"bb22cc33dd44ee55","correlation_id":"run-20261024-1412","event":"booking.request.timeout","message":"booking-service did not answer expirar in 3000 ms, retry 1 of 3","job":"expire-stale-reservations","reserva_id":"8e2f6a1c-3b4d-4e5f-9a0b-1c2d3e4f5a6b","duration_ms":3000,"attempt":1}
{"timestamp":"2026-10-24T14:12:04.091Z","level":"INFO","service":"booking-service","version":"2.1.0","environment":"main","trace_id":"1c7e4a9b2d3f4e5a8b6c7d8e9f0a1b2c","span_id":"cc33dd44ee55ff66","correlation_id":"run-20261024-1412","event":"reserva.expired","message":"reservation moved to EXPIRADA, ReservaExpirada written to outbox","reserva_id":"8e2f6a1c-3b4d-4e5f-9a0b-1c2d3e4f5a6b","motivo_cierre":"TIMEOUT_PAGO_EXCEDIDO","duration_ms":44}
{"timestamp":"2026-10-24T14:12:07.908Z","level":"INFO","service":"property-worker","version":"2.0.0","environment":"main","trace_id":"1c7e4a9b2d3f4e5a8b6c7d8e9f0a1b2c","span_id":"aa11bb22cc33dd44","correlation_id":"run-20261024-1412","event":"job.run.finished","message":"run finished: 2 expired, 0 skipped, 0 failed","job":"expire-stale-reservations","duration_ms":7904,"expired":2,"skipped":0,"failed":0}
```

## Where the SLIs end up

- **SLO 1, job runs:** 40 runs, **0 failed**. Retries saved every reservation (an item fails
  only if its 3 attempts are all slow: 16 × 0.25³ ≈ 0.25). **100 %.**
- **SLO 2, `expirar` ≤ 0.25 s:** about 21 calls with the retries, **5 slow**. **76 %** against 99 %.
- **SLO 3, nothing stuck > 20 min:** the 16 reservations were expired within 3 minutes.
  **100 %.**

## First alert

- `ExpiryJobFastBurn` and `ExpiryJobNotRunning`: **silent** (no failed runs, runs keep finishing).
- `ExpirarLatencyBurn` (`for: 0m`): all 16 reservations are handled in the first runs,
  so both the 5 min and the 1 h window are at ~24 % slow (> 14.4 %) by the **2nd minute**:
  it fires at **14:12** as a ticket. It resolves by itself around 14:18 — there are no
  more calls, so the 5 min window has no data. The slowness was still there, only hidden.

## Runbook step that stops it

"Read one failed run" shows the time inside Booking: `INSERT booking.outbox_event` waits.
Booking's own logs show the new relay locking the table. **Step 2, rollback of
`booking-service`** to `v2.0.0`, ends it.

## Postmortem (blameless)

| Time (2026-10-24) | What happened |
|---|---|
| 13:58 | `booking-service` `v2.1.0` deployed; its outbox relay now takes a table lock on `booking.outbox_event` for each batch (about 3 s every 12 s) |
| 14:05–14:10 | Stripe hiccup; 16 Sagas cannot finish their compensation, so 16 reservations stay `PENDIENTE` |
| 14:10 | Drill window starts: 1 of 4 inserts in the outbox waits for the lock |
| 14:12 | `ExpirarLatencyBurn` ticket opens |
| 14:30 | On-call reads a run trace, sees the wait on `outbox_event` |
| 14:47 | Rollback of `booking-service` |
| 14:50 | `expirar` back to ~40 ms |

**Impact.** No reservation stuck, no guest affected by the expiry. But SLO 2 is **broken
for the month**: 5 slow calls against a budget of 1.2 (**more than 400 %**). In minutes,
40 × 25 % = 10 of 432 = 2.3 %; with so few calls the count of calls is the honest number.
Policy at 100 %: freeze of the `expirar` endpoint except fixes.

**Root cause.** The relay of `v2.1.0` changed `SELECT … FOR UPDATE SKIP LOCKED` for a
lock on the whole table, so every `INSERT` in `outbox_event` waited while a batch was
being published.

**Contributing factors.**
- The same lock slowed `confirmar-reserva` of the Saga; nobody linked the two signals.
- `ExpirarLatencyBurn` resolved alone when calls stopped, which looked like "fixed".
- No test in CI runs the relay and inserts at the same time.

**Action items as user stories.**

1. **Story:** as the Booking owner I want a test that publishes outbox batches while
   inserting new events, so that a table lock never reaches `main`.
   **Acceptance:** given the relay publishing 500 rows, when 50 `expirar` calls run at the
   same time, then each one answers in under 250 ms.
2. **Story:** as on-call I want an alert on the age of the oldest unpublished outbox row,
   so that a stuck relay is seen even when nobody calls `expirar`.
   **Acceptance:** given `booking_outbox_pending_events` > 0 and its oldest row older than
   60 s for 5 min, when Prometheus evaluates, then `OutboxRelayStuck` fires as a ticket.
3. **Story:** as on-call I want `ExpirarLatencyBurn` to stay open while the slow ratio is
   unknown, so that "no calls" is not read as "fixed".
   **Acceptance:** given the alert is firing and the 5 min window has no data, when the
   rule is evaluated, then the alert keeps firing until 5 calls in a row are fast.
