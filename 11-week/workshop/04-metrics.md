# CP4 · Golden signals and metric catalog (expiry journey)

Conventions: `snake_case`, unit suffix (`_seconds`), `_total` for counters.
`service`, `environment` and `version` come from the scrape configuration.

## property-worker — job `expire-stale-reservations`

A job has no users waiting, so "latency" is the length of a run and "traffic" is runs
and reservations processed.

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `worker_job_run_duration_seconds` | histogram | `job` | `histogram_quantile(0.95, sum by (le) (rate(worker_job_run_duration_seconds_bucket{job="expire-stale-reservations"}[15m])))` |
| Traffic | `worker_job_items_total` | counter | `job`, `result` (`expired`, `skipped`, `failed`) | `sum by (result) (increase(worker_job_items_total{job="expire-stale-reservations"}[1h]))` |
| Errors | `worker_job_runs_total` | counter | `job`, `outcome` (`ok`, `partial`, `failed`) | `sum(rate(worker_job_runs_total{outcome="failed"}[15m])) / sum(rate(worker_job_runs_total[15m]))` |
| Saturation | `worker_job_backlog` | gauge | `job` | `max(worker_job_backlog{job="expire-stale-reservations"})` — candidates left for the next run because of `BATCH_SIZE` |

Also: `worker_job_last_success_timestamp_seconds{job}` →
`time() - worker_job_last_success_timestamp_seconds{job="expire-stale-reservations"} > 300`
means the job stopped running (no runs, no errors either).

## booking-service — E-19 `expirar` and the outbox

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `booking_expire_duration_seconds` | histogram | — | `histogram_quantile(0.95, sum by (le) (rate(booking_expire_duration_seconds_bucket[5m])))` |
| Traffic | `booking_expirations_total` | counter | `result` (`expired`, `already_expired`, `invalid_transition`, `not_found`) | `sum by (result) (rate(booking_expirations_total[5m]))` |
| Errors | `booking_http_requests_total` | counter | `route`, `status_class` | `sum(rate(booking_http_requests_total{route="/api/v1/reservas/{id}/expirar",status_class="5xx"}[5m]))` |
| Saturation | `booking_outbox_pending_events` | gauge | — | `max(booking_outbox_pending_events)` — rows not yet published; growing means the relay or RabbitMQ is stuck |

`invalid_transition` (422) is expected when the Saga confirmed the reservation between
the worker's read and its `expirar`: counted, not an error.

## Stale reservations (business signal)

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Saturation | `booking_reservas_pending_oldest_age_seconds` | gauge | — | `max(booking_reservas_pending_oldest_age_seconds) > 1200` — a `PENDIENTE` older than 20 min means the safety net is not working |

## RabbitMQ and consumers of `ReservaExpirada`

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `consumer_event_lag_seconds` | histogram | `consumer` (`catalog`, `notification`) | `histogram_quantile(0.95, sum by (le, consumer) (rate(consumer_event_lag_seconds_bucket{event_type="ReservaExpirada"}[5m])))` |
| Traffic | `rabbitmq_queue_messages_published_total` | counter | `queue` | `sum by (queue) (rate(rabbitmq_queue_messages_published_total[5m]))` |
| Errors | `rabbitmq_queue_messages_ready` on DLQ | gauge | `queue` | `max(rabbitmq_queue_messages_ready{queue=~".*\\.dlq"}) > 0` |
| Saturation | `rabbitmq_queue_messages_ready` | gauge | `queue` | `max by (queue) (rabbitmq_queue_messages_ready)` |

## Cardinality

| Label | Values | Count |
|---|---|---|
| `job` | jobs of `property-worker` | 1 today, < 5 later |
| `result` / `outcome` | closed lists above | 3–4 |
| `route` | route template | ~10 |
| `status_class` | 2xx, 4xx, 5xx | 3 |
| `consumer`, `queue`, `event_type` | closed lists | < 10 |

Not labels: `reserva_id`, `propiedad_id`, run id, `trace_id`, raw path with the id. They
are in the logs (CP2); a metric with `reserva_id` would create one series per booking.

## Buckets

- `booking_expire_duration_seconds`: `0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 1` — `expirar` is
  one `UPDATE` and one `INSERT` in one transaction, so it should take tens of ms.
- `worker_job_run_duration_seconds`: `0.5, 1, 5, 10, 30, 60` — the job runs every minute
  and stops at `RUN_TIMEOUT`; a run longer than 60 s overlaps the next one.
