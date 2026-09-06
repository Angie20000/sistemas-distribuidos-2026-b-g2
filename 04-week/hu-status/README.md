<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       01-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 04

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: property
- SPRINT_GOAL: Complete the `03-product` documentation folder in the project repository
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-DOC-003 | Write the product vision (`03-product/vision.md`) | done | *(add PR/commit link)* |
| HU-DOC-004 | Write the problem framing (`03-product/problem-framing.md`) | done | *(add PR/commit link)* |

## 2. My individual contribution
- Wrote `03-product/vision.md` (vision statement, team mission, strategic pillars, roadmap, product principles, and Definition of Done for MVP 1) and `03-product/problem-framing.md` (problem statement, affected users, solution hypothesis, success metrics, and risks), keeping both consistent with the existing PDR, scope, overview, glossary, and domain-map docs.

## 3. Blockers and risks
- Found a naming inconsistency between the team's own docs: `glossary.md` mandates English identifiers (`BookingCreated`, `PaymentRejected`), while `entities-and-rules.md` and `domain-events.md` use Spanish names (`ReservaCreada`, `PagoRechazado`) with matching Java code. Needs a team decision on which convention actually applies.

## 4. Plan for next week
- Prepare and upload the UI mockup of the platform to the project repository.

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

> Note: documentation-only deliverable — code-specific checks don't apply this week.

## 6. Evidence links
- *(add link to vision.md and problem-framing.md commits/PRs in the repo)*