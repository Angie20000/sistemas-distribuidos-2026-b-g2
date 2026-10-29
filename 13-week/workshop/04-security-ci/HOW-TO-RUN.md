# How to run the negative privilege test — property-booking-db

Story: **[security] booking_app cannot delete or change structure** (STRIDE E).
Acceptance criteria covered: *as booking_app, DELETE FROM booking.reservation and CREATE
TABLE in schema booking fail with insufficient_privilege, checked on every pull request.*

Table: `booking.reservation`. User: `booking_app` (role `booking_writer`, A3).

1. In your clone of `code-corhuila/property-booking-db` (branch `develop`, with A1–A3
   merged), run `verificar-db.sh` so the schema and the roles exist, but stop before the
   rollback — or start PostgreSQL with `property-infra-postgres` and run `booking-db-migrate`.
2. Run the test as the admin of the instance:
   ```bash
   psql -h localhost -U property_admin -d property -v ON_ERROR_STOP=1 -f negative_booking_app_privileges.sql
   ```
3. Expected: two notices, `OK: booking_app cannot DELETE reservations` and
   `OK: booking_app cannot create tables`, and exit code 0.

The real console output is added in CP8.
