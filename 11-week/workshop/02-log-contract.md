# CP2 · Structured log contract (expiry journey)

`property-worker`, `booking-service` and the consumers of `ReservaExpirada` write one
JSON object per line to stdout with the same field names. A run of the job is one
trace, so filtering by `trace_id` shows everything one run did.

## Mandatory fields

| Field | Type | Required | Example | Why |
|---|---|---|---|---|
| `timestamp` | ISO-8601 UTC | yes | `2026-10-19T15:03:04.310Z` | The job runs every minute: order matters to see overlapping runs |
| `level` | `ERROR` `WARN` `INFO` `DEBUG` | yes | `ERROR` | Severity |
| `service` | string | yes | `property-worker` | Who wrote it |
| `version` | string | yes | `2.0.0` | Release tag |
| `environment` | `dev` `qa` `main` | yes | `qa` | Our environments |
| `trace_id` | 32 hex | yes | `9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8` | One run of the job, end to end |
| `span_id` | 16 hex | yes | `3d4e5f6071829304` | One call inside the run |
| `correlation_id` | string | yes | `run-20261019-1503` | For the worker, the run id; for HTTP requests, `X-Correlation-Id` |
| `event` | `noun.verb` | yes | `reserva.expired` | Stable name |
| `message` | string | yes | `reservation moved to EXPIRADA` | For people |
| `duration_ms` | integer | on calls and on the end of a run | `3002` | Latency per hop |

## Domain fields

| Field | Type | Example | Why |
|---|---|---|---|
| `job` | string | `expire-stale-reservations` | The worker will have more jobs; filter by job |
| `reserva_id` | uuid | `3c8e2a71-…` | The `Reserva` expired or skipped |
| `estado_actual` | `PENDIENTE` `CONFIRMADA` `CANCELADA` `EXPIRADA` | `CONFIRMADA` | Explains a `422 INVALID_STATUS_TRANSITION` |
| `motivo_cierre` | `TIMEOUT_PAGO_EXCEDIDO` … | `TIMEOUT_PAGO_EXCEDIDO` | Closure reason saved in `reservation.closure_reason` |
| `propiedad_id` | string | `prop-118` | Which property gets its dates back |
| `candidates`, `expired`, `skipped`, `failed` | integer | `2`, `1`, `1`, `0` | Summary of the run; `failed > 0` is the signal to look at |
| `attempt` | integer 1–3 | `1` | Retry number of a call from the worker |

## Never log

| Data | Where | Masking |
|---|---|---|
| `SERVICE_TOKEN` of `svc-worker` | `Authorization` header of every worker call | Header removed; log `auth=service` |
| Guest data (`usuarioId`, e-mail, name) | `ReservaResponse`, `ReservaExpirada` payload | Not logged; `reserva_id` is enough to find it |
| Payment data | not part of this journey | — |
| Full response body of Booking | worker | Only `estado` and status code |

## Seven lines of one run with failures

One run finds two old `PENDIENTE` reservations. The first call times out and the retry
works; the second reservation was confirmed in the meantime, so Booking rejects it.

```json
{"timestamp":"2026-10-19T15:03:00.002Z","level":"INFO","service":"property-worker","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"0a1b2c3d4e5f6071","correlation_id":"run-20261019-1503","event":"job.run.started","message":"expire-stale-reservations run started","job":"expire-stale-reservations","batch_size":50,"pending_ttl_minutes":15}
{"timestamp":"2026-10-19T15:03:00.091Z","level":"INFO","service":"property-worker","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"1b2c3d4e5f607182","correlation_id":"run-20261019-1503","event":"job.candidates.found","message":"2 PENDIENTE reservations older than 15 minutes","job":"expire-stale-reservations","duration_ms":87,"candidates":2}
{"timestamp":"2026-10-19T15:03:03.096Z","level":"ERROR","service":"property-worker","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"2c3d4e5f60718293","correlation_id":"run-20261019-1503","event":"booking.request.timeout","message":"booking-service did not answer POST /api/v1/reservas/{id}/expirar in 3000 ms, retry 1 of 3","job":"expire-stale-reservations","reserva_id":"3c8e2a71-5d4f-4b9a-8e6c-0a1b2c3d4e5f","duration_ms":3002,"attempt":1}
{"timestamp":"2026-10-19T15:03:04.310Z","level":"INFO","service":"booking-service","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"3d4e5f6071829304","correlation_id":"run-20261019-1503","event":"reserva.expired","message":"reservation moved to EXPIRADA, ReservaExpirada written to outbox","reserva_id":"3c8e2a71-5d4f-4b9a-8e6c-0a1b2c3d4e5f","propiedad_id":"prop-118","motivo_cierre":"TIMEOUT_PAGO_EXCEDIDO","duration_ms":41}
{"timestamp":"2026-10-19T15:03:04.402Z","level":"WARN","service":"booking-service","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"4e5f607182930415","correlation_id":"run-20261019-1503","event":"reserva.expire.rejected","message":"reservation is CONFIRMADA, cannot expire","reserva_id":"7d1f4b92-6e5a-4c0b-9f7d-1b2c3d4e5f60","estado_actual":"CONFIRMADA","error_code":"INVALID_STATUS_TRANSITION","duration_ms":9}
{"timestamp":"2026-10-19T15:03:04.405Z","level":"WARN","service":"property-worker","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"5f60718293041526","correlation_id":"run-20261019-1503","event":"job.item.skipped","message":"422 INVALID_STATUS_TRANSITION counted, not retried","job":"expire-stale-reservations","reserva_id":"7d1f4b92-6e5a-4c0b-9f7d-1b2c3d4e5f60"}
{"timestamp":"2026-10-19T15:03:04.410Z","level":"WARN","service":"property-worker","version":"2.0.0","environment":"qa","trace_id":"9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8","span_id":"0a1b2c3d4e5f6071","correlation_id":"run-20261019-1503","event":"job.run.finished","message":"run finished: 1 expired, 1 skipped, 0 failed","job":"expire-stale-reservations","duration_ms":4408,"expired":1,"skipped":1,"failed":0}
```

## Levels

| Level | When | Example |
|---|---|---|
| `ERROR` | A call failed or a run could not finish | `booking.request.timeout`; a run that hits `RUN_TIMEOUT` |
| `WARN` | Expected, handled, but worth a look | `reserva.expire.rejected` (422), `job.item.skipped`, a run that ends with `skipped > 0` |
| `INFO` | Normal facts | `job.run.started`, `reserva.expired` |
| `DEBUG` | Local only | The `creadaAntesDe` value computed for the query |
