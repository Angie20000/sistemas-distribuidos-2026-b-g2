-- Story: [security] booking_app cannot delete or change structure (STRIDE E).
-- Run after the migrations of property-booking-db, as the instance admin.
-- Each block must fail with insufficient_privilege (42501); if it succeeds, the test fails.
SET ROLE booking_app;

DO $$
BEGIN
  DELETE FROM booking.reservation;
  RAISE EXCEPTION 'booking_app was able to DELETE from booking.reservation';
EXCEPTION WHEN insufficient_privilege THEN
  RAISE NOTICE 'OK: booking_app cannot DELETE reservations';
END $$;

DO $$
BEGIN
  EXECUTE 'CREATE TABLE booking.not_allowed (id int)';
  RAISE EXCEPTION 'booking_app was able to CREATE a table in schema booking';
EXCEPTION WHEN insufficient_privilege THEN
  RAISE NOTICE 'OK: booking_app cannot create tables';
END $$;

RESET ROLE;
