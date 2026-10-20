# CP7 · Alert policy (expiry journey)

A 30-day window has 720 hours. An alert with burn rate *B* and long window *W* fires
after spending *B × W / 720* of the budget.

| Alert | Rule | Budget used when it fires | With our numbers |
|---|---|---|---|
| `ExpiryJobFastBurn` (page) | > 14.4 % of runs failed over 1 h and 5 min | 14.4 × 1 / 720 = **2 %** | 2 % of 432 runs = **8.6 failed runs ≈ 8.6 minutes** |
| `ExpiryJobSlowBurn` (ticket) | > 6 % of runs failed over 6 h and 30 min | 6 × 6 / 720 = **5 %** | 5 % of 432 = **21.6 runs ≈ 21.6 minutes** |
| `ExpirarLatencyBurn` (ticket) | > 14.4 % of `expirar` calls over 0.25 s, 1 h and 5 min | **2 %** of SLO 2 | 0.02 slow calls / **8.6 minutes** — with ~4 calls a day, in practice one slow call in the hour is enough |
| `ExpiryJobNotRunning` (page) | no successful run for 5 min | — | protects SLO 3: 5 of its 216 minutes |

**Why `ExpiryJobNotRunning` exists.** Burn-rate alerts divide failed runs by all runs.
If the worker container is down there are **no runs**, so the ratio is empty and the
burn-rate alerts stay silent while reservations pile up in `PENDIENTE`. The time since
the last success catches that. It pages because SLO 3 (216 minutes) would be gone in
less than four hours.

**Why the latency alert is a ticket.** A slow `expirar` still expires the reservation;
the guest does not notice a few seconds. It tells us Booking has a problem (locks in
`booking.reservation` or `booking.outbox_event`) before it hurts the Saga.

## Alerts we will not create

1. **"`expirar` answered 422 `INVALID_STATUS_TRANSITION`".** It happens when the Saga
   confirms the reservation between the worker's read and its call. That is the race the
   design accepts: Booking is the authority and the worker skips it. Paging on it would
   wake someone for the system working as designed.
2. **"PostgreSQL CPU above 80 %" on the booking schema.** The expiry is a tiny part of
   Booking's load. High CPU may come from searches of "Mis reservas" and does not mean
   reservations are stuck. If it does slow `expirar`, `ExpirarLatencyBurn` says so with
   the symptom we care about.
