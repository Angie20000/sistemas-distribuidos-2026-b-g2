<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       01-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 01

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: property
- SPRINT_GOAL: Define and document the Preliminary Design Review (PDR) for MVP 1
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-DOC-001 | Draft the Preliminary Design Review (PDR v1.0 - MVP 1 Definition) | done | *(add PR/commit link)* |

## 2. My individual contribution
- Participated in drafting the PDR as a team, defining the three Bounded Contexts (Catalog, Booking, Payment), the CAP/PACELC consistency trade-offs, and the choreographed Saga flow for the "Place Order" process.

## 3. Blockers and risks
- None specific to this week — the PDR left several implementation details open (message broker choice, payment gateway, deployment strategy), to be resolved in later weeks.

## 4. Plan for next week
- Define the team's working methodology (Kanban/Scrum) before starting on the project repository documentation.

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

> Note: this week's deliverable was a design document (the PDR), not code, so the code-related checks (commits, branching, tests, hexagonal boundaries) don't fully apply yet — mark/adjust once your actual repo workflow for this week is confirmed.

## 6. Evidence links
- *(add link to the PDR file/commit in the repo)*