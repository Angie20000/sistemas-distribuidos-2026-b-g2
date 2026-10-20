# CP8 · Runbook — ExpiryJobFastBurn

**Page.** More than 14.4 % of the runs of `expire-stale-reservations` failed in the last
hour and in the last 5 minutes. Unpaid reservations keep their dates locked; guests see
those dates as taken in the Catalog portal.

## Symptoms

- Logs of `property-worker` with `"event":"job.run.finished"` and `failed > 0`, or
  `"event":"booking.request.timeout"`.
- `booking_reservas_pending_oldest_age_seconds` growing past 900 (15 minutes).
- Maybe also `ExpirarLatencyBurn` (Booking slow) or `ExpiryJobNotRunning`.

## How to confirm

**a. Look at the numbers**

```promql
sum by (outcome) (increase(worker_job_runs_total{job="expire-stale-reservations"}[15m]))
sum by (result) (increase(worker_job_items_total{job="expire-stale-reservations"}[15m]))
max(booking_reservas_pending_oldest_age_seconds) / 60
histogram_quantile(0.95, sum by (le) (rate(booking_expire_duration_seconds_bucket[15m])))
```

**b. Read one failed run.** Each run is one trace (CP1), so take the `trace_id` of a run
with `failed > 0` and read the whole run, worker and Booking together:

```bash
docker compose logs --since 20m property-worker | grep '"event":"job.run.finished"' | grep -v '"failed":0'
docker compose logs --since 20m property-worker booking-service | grep '"trace_id":"<trace_id>"'
```

Read it in order: timeouts against Booking point to Booking; a `401`/`403` points to the
worker's token (`svc-worker`); `5xx` from Booking points to its database.

## What to do, least risky first

**1. Make the runs smaller.** Lower `BATCH_SIZE` (for example from 50 to 10) and restart
only `property-worker`. Each run asks Booking for less, so it ends before `RUN_TIMEOUT`.
Nothing is lost: what is left waits for the next minute (ADR-014).

**2. Roll back** the service whose release matches the start of the failures
(`property-worker` or `booking-service`): previous image tag in `property-infra`, then
`docker compose up -d <service>`.

**3. Scale `booking-service`** if it is saturated: `docker compose up -d --scale
booking-service=2`. **Do not** scale `property-worker`: two workers would run the job
twice at the same time; it is safe because `expirar` is idempotent, but it doubles the
load on Booking during an incident.

If the worker cannot be fixed fast: **pause the job** (`EXPIRY_JOB_ENABLED=false`) and
expire by hand the reservations older than 20 minutes, one by one, with
`POST /api/v1/reservas/{id}/expirar` and the service token.

## Who to call

1. **On-call of the week** — confirms and applies step 1 (minutes 0–10).
2. **Owner of the worker and of the `expirar` endpoint** (Booking) — rollback or scale
   (after 15 minutes).
3. **Whole team** — if reservations stay stuck more than 30 minutes (SLO 3 budget running
   out), or if a Saga and the worker disagree on the state of a reservation.

## Recovery check

- Five runs in a row with `failed=0` in `job.run.finished`, and the page resolves.
- `max(booking_reservas_pending_oldest_age_seconds)` back under 900.
- The expired reservations published `ReservaExpirada` (look for `outbox.event.published`
  with the same `trace_id` as the run) and Catalog freed their dates.
- `BATCH_SIZE` and any paused flag are back to their normal values.
