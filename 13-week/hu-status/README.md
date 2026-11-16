<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       13-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 13

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Group 2 — Property
- SPRINT_GOAL: Finish the base of booking-api and threat model the boundary booking-api -> PostgreSQL (schema booking), turning its top risks into security stories
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| #161 | HU-BOOK-001 — Create a Reserva and lock its dates | doing | https://github.com/code-corhuila/property-booking-api/pull/2 |
| #168 | Threat model: booking-api -> PostgreSQL (schema booking) | done | https://github.com/code-corhuila/property-docs/pull/173 |
| #174 | [security] booking_app cannot delete or change structure | todo | https://github.com/code-corhuila/property-docs/issues/174 |
| #175 | [security] Only booking reaches the reservations | todo | https://github.com/code-corhuila/property-docs/issues/175 |
| #176 | [security] Secrets of the booking database never reach the repository | todo | https://github.com/code-corhuila/property-docs/issues/176 |

## 2. My individual contribution
- Reserva domain model and its invariants: https://github.com/code-corhuila/property-booking-api/pull/2
- Common HTTP adapters (error envelope, correlation, JWT): https://github.com/code-corhuila/property-booking-api/pull/3
- Versioned OpenAPI contract of booking-api: https://github.com/code-corhuila/property-booking-api/pull/4
- Attack surface and STRIDE of my boundary: https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/tree/main/13-week/workshop and https://github.com/code-corhuila/property-docs/pull/173
- Three security stories in the backlog: #174, #175, #176

## 3. Blockers and risks
- The CI of the code-corhuila organization does not start (billing lock). With the instructor's authorization, PRs are merged with one peer approval plus the local test output posted in the PR.

## 4. Plan for next week
- Plan batch 2 of booking: persistence, the catalog client and the endpoints of the Saga.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration)
- [x] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

## 6. Evidence links
- https://github.com/code-corhuila/property-booking-api/pull/2
- https://github.com/code-corhuila/property-booking-api/pull/3
- https://github.com/code-corhuila/property-booking-api/pull/4
- Workshop: https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/tree/main/13-week/workshop
- STRIDE PR: https://github.com/code-corhuila/property-docs/pull/173
- Claim issue: https://github.com/code-corhuila/property-docs/issues/168
- STRIDE pull request commits: https://github.com/code-corhuila/property-docs/pull/173/commits
- Review comment I left on a teammate's pull request: https://github.com/code-corhuila/property-docs/pull/169/changes#r4419921452
- Review received from Daniel: https://github.com/code-corhuila/property-docs/pull/173#issuecomment-6513866556
- Commits of the workshop in my fork: https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/commits/main/13-week
- Security CI proposal: https://github.com/Angie20000/sistemas-distribuidos-2026-b-g2/tree/main/13-week/workshop/04-security-ci
