<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       14-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 14

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Group 2 — Property
- SPRINT_GOAL: Plan batch 2 of Booking - persistence, the catalog client and the endpoints the Saga calls - and open its stories
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| #188 | HU-BOOK-002 — Prevent double-booking under concurrency | doing | https://github.com/code-corhuila/property-docs/issues/188 |
| #189 | HU-BOOK-003 — Confirm a Reserva | doing | https://github.com/code-corhuila/property-docs/issues/189 |
| #190 | HU-BOOK-004 — Cancel a Reserva and release its dates | doing | https://github.com/code-corhuila/property-docs/issues/190 |

## 2. My individual contribution
- Opened the issues of my batch 2 stories: #188, #189, #190.
- Started preparing the booking-api changes of batch 2 so the pull requests can be opened in week 15.

## 3. Blockers and risks
- Batch 2 depends on the batch 1 pull requests of every service being merged first.
- The organization CI is still blocked; evidence is the local test output.

## 4. Plan for next week
- Open and merge the booking-api pull requests of batch 2.
- Batch 3: the outbox relay and the expiry worker.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(no pull request this week; they open in week 15)*
- [x] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration) — *(planning week; tests come with the batch 2 pull requests)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code merged this week)*
- [x] No secrets; config via environment variables

## 6. Evidence links
- https://github.com/code-corhuila/property-docs/issues/188
- https://github.com/code-corhuila/property-docs/issues/189
- https://github.com/code-corhuila/property-docs/issues/190
