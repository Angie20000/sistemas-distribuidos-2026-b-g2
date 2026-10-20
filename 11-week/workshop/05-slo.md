# CP6 · SLOs for the expiry of unpaid reservations

The journey (CP1) has no guest waiting on a screen. What a guest feels is the
**result**: dates locked by a `PENDIENTE` reservation that nobody will pay. So the SLOs
watch whether the job `expire-stale-reservations` of `property-worker` runs, whether
`POST /api/v1/reservas/{id}/expirar` (E-19) of `booking-service` answers fast, and
whether stuck reservations are cleaned in time.

## Traffic

- The job runs **every minute**: 60 × 24 × 30 = **43,200 runs** per 30 days.
- With the orchestrated Saga a `Reserva` stays `PENDIENTE` only when something broke
  (ADR-014). We guess **2 % of the 6,000 booking attempts** a month (team estimate of
  200 a day): **120 calls to `expirar`** per 30 days, plus a few `422` when the Saga
  confirmed first.
- Window: **30 days = 43,200 minutes**.

## SLO 1 — the job runs and finishes

| | |
|---|---|
| SLI | runs with `outcome` `ok` or `partial` / all runs |
| PromQL | `sum(increase(worker_job_runs_total{job="expire-stale-reservations",outcome!="failed"}[30d])) / sum(increase(worker_job_runs_total{job="expire-stale-reservations"}[30d]))` |
| Target | **99 %** |
| Budget | 1 % × 43,200 = **432 failed runs**; since one run is one minute, **432 minutes** |

A `partial` run (some items `skipped` by a `422`) is a good run: the job did its work.

## SLO 2 — `expirar` is fast

| | |
|---|---|
| SLI | `expirar` calls answered in 0.25 s or less / all `expirar` calls |
| PromQL | `sum(increase(booking_expire_duration_seconds_bucket{le="0.25"}[30d])) / sum(increase(booking_expire_duration_seconds_count[30d]))` |
| Target | **99 %** — it is one `UPDATE` plus one `INSERT` in the outbox |
| Budget | 1 % × 120 = **1.2 slow calls**; 1 % × 43,200 = **432 minutes** |

With so few calls the budget in calls is tiny; one slow call already uses most of it.
That is honest: we expect `expirar` to be fast every time.

## SLO 3 — no reservation stays stuck (our extra SLO)

The rule of the domain is 15 minutes. With the job every minute and its retries, we
accept up to **20 minutes**. Above that, the safety net failed.

| | |
|---|---|
| SLI | minutes in which the oldest `PENDIENTE` is younger than 20 min / all minutes |
| PromQL | `avg_over_time((max(booking_reservas_pending_oldest_age_seconds) < bool 1200)[30d:1m])` |
| Target | **99.5 %** |
| Budget | 0.5 % × 43,200 = **216 minutes** a month with a reservation stuck more than 20 min |

This one is time-based, not request-based, because the harm (dates locked) grows with
time, not with the number of calls.

## Budget table

| SLO | Target | Budget | In minutes |
|---|---|---|---|
| 1 — job runs | 99 % | 432 runs | 432 |
| 2 — `expirar` ≤ 0.25 s | 99 % | 1.2 calls | 432 |
| 3 — nothing stuck > 20 min | 99.5 % | — | 216 |

## What we do as the budget is spent

1. **Half the budget gone (50 %).** Post it in the team chat with the graph. The owner of
   `property-worker` reviews the last runs with `failed > 0`.
2. **75 % gone.** Changes to `property-worker` and to the `expirar` endpoint of Booking need
   a second reviewer and a tested rollback. No new jobs are added to the worker.
3. **All of it gone (100 %).** Freeze the worker and the endpoint, except fixes. For SLO 3,
   the on-call lists the stuck reservations every morning and expires them by hand
   (`POST /api/v1/reservas/{id}/expirar` with the service token) until the job is healthy.
