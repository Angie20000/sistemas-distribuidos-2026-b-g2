# CP3 · How to run (booking-service, expiry)

Java 17+ module of `booking-service`, no framework. It covers the hop where
`property-worker` calls `POST /api/v1/reservas/{id}/expirar` and the async boundary:

- `TracingHandler` wraps the handler, continues the worker's `traceparent` (or starts one),
  answers it back and logs the request.
- `LogLine` writes the CP2 contract.
- `OutboxRelay` stores the `traceparent` in the outbox row and copies it into the headers
  of the `ReservaExpirada` message — the outbound call of this journey.
- `ExpiryMetrics`: `booking_expirations_total{result}` and
  `booking_expire_duration_seconds` on `/metrics`.

## Test

```bash
cd 11-week/workshop/03-instrumentation
mvn test
```

Without Maven (PowerShell, Docker Desktop running):

```powershell
docker run --rm -v "${PWD}:/app" -w /app maven:3.9-eclipse-temurin-21 mvn test
```

`ExpiryTraceTest` sends `expirar` with the worker's `traceparent` and checks that the
published `ReservaExpirada` carries the same `trace_id` with a new span id; a second
test checks that a `CONFIRMADA` reservation gets `422` and is counted.

## Run

```bash
mvn -q compile
java -cp target/classes co.edu.corhuila.property.booking.expiry.ExpiryServer
curl -i -X POST http://localhost:3001/api/v1/reservas/3c8e2a71-5d4f-4b9a-8e6c-0a1b2c3d4e5f/expirar -H "traceparent: 00-9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8-2c3d4e5f60718293-01"
curl http://localhost:3001/metrics
```

## Console output on my machine

```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  41.127 s
[INFO] Finished at: 2026-10-19T22:23:45Z
[INFO] ------------------------------------------------------------------------
```
