<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       11-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 11

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Pms_Property
- SPRINT_GOAL: Make the expiry of unpaid reservations observable — from the job of property-worker to Booking, the outbox and the consumers of ReservaExpirada — and start corte 3 with the real services.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| W11-CP1 | Trace map of the expiry: property-worker, booking-service, outbox, RabbitMQ, Catalog and Notification | done | https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/01-journey.md |
| W11-CP2 | JSON log contract for the job runs and the `expirar` endpoint | done | https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/02-log-contract.md |
| W11-CP3 | Java tracing handler, logger, trace stored in the outbox and copied to the message, `/metrics` | done | https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/tree/main/11-week/workshop/03-instrumentation |
| W11-CP4 | Golden signals for the worker job, Booking's expiry and the consumers | done | https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/04-metrics.md |
| W11-CP6..CP9 | SLOs, alerts, runbook and incident drill | todo | — |

## 2. My individual contribution
- Mapped the expiry journey, which has no user request: each run of the job starts its own trace.
- Defined the log contract for job runs (`expired`, `skipped`, `failed`) and for `expirar`.
- Wrote and tested a Java module of `booking-service` where the `trace_id` of the worker survives the outbox and reaches the `ReservaExpirada` message.
- Wrote the metric catalog, including a signal for when the job silently stops running.

## 3. Blockers and risks
- The worker and Booking are not built yet; the module is standalone until corte 3.
- `booking.outbox_event` needs a `traceparent` column, not yet in `06-data/models.md`.

## 4. Plan for next week
- Delivery 2 of the workshop (CP6 to CP10).
- Corte 3: the real job `expire-stale-reservations` in `property-worker`.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(individual workshop in my fork)*
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration) — *(`ExpiryTraceTest`)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables — *(the worker's service token is in the never-log list)*

## 6. Evidence links
- Workshop: https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/tree/main/11-week/workshop
- property-docs: https://github.com/code-corhuila/property-docs
