<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       12-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 12

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Group 2 — Property
- SPRINT_GOAL: Start the code of cut 3 for Booking - the booking database (schema, tables and roles) and the skeleton of booking-api
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| #161 | HU-BOOK-001 — Create a Reserva and lock its dates | doing | https://github.com/code-corhuila/property-booking-db/pull/2 |

## 2. My individual contribution
- Liquibase repository of property-booking-db: https://github.com/code-corhuila/property-booking-db/pull/1
- Schema `booking` with its tables and the exclusion constraint of INV-001: https://github.com/code-corhuila/property-booking-db/pull/2
- Roles and grants of `booking_app`: https://github.com/code-corhuila/property-booking-db/pull/3
- Three-module Spring Boot skeleton of booking-api: https://github.com/code-corhuila/property-booking-api/pull/1

## 3. Blockers and risks
- The CI of the code-corhuila organization does not start (billing lock). With the instructor's authorization, PRs are merged with one peer approval plus the local test output posted in the PR.

## 4. Plan for next week
- Domain model of the Reserva with its invariants, the common HTTP adapters and the OpenAPI contract of booking-api.
- Threat modeling workshop for the boundary booking-api -> PostgreSQL.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration)
- [x] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

## 6. Evidence links
- https://github.com/code-corhuila/property-booking-db/pull/1
- https://github.com/code-corhuila/property-booking-db/pull/2
- https://github.com/code-corhuila/property-booking-db/pull/3
- https://github.com/code-corhuila/property-booking-api/pull/1
