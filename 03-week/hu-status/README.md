<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       01-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 03

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: property
- SPRINT_GOAL: Complete the `01-context` documentation folder in the project repository
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-DOC-002 | Write the project glossary (`01-context/glossary.md`) | done | *(add PR/commit link)* |

## 2. My individual contribution
- Wrote `01-context/glossary.md`: the official term dictionary for the project, covering domain terms (Booking, Ledger, Temporary Inventory Lock, etc.), technical terms (Hexagonal Architecture, Choreographed Saga, Idempotence, CAP/PACELC), and acronyms, keeping code-facing identifiers in English per the team's naming convention.

## 3. Blockers and risks
- The glossary's convention (English identifiers for events/states) later turned out to be inconsistent with the more detailed domain docs (`entities-and-rules.md`, `domain-events.md`), which use Spanish names (`Reserva`, `PagoRechazado`, etc.) — this needs to be reconciled with the team.

## 4. Plan for next week
- Move on to the `03-product` folder, covering the product vision and problem framing.

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

> Note: this was a documentation-only deliverable, so the code-specific checks (branching, tests, hexagonal boundaries) don't apply — confirm your actual commit/PR flow before checking those boxes.

## 6. Evidence links
- *(add link to the glossary.md file/commit in the repo)*