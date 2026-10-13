<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       08-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 08

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Pms_Property
- SPRINT_GOAL: Start the API contract layer — first drafts of the notification and payment OpenAPI files, the API guidelines for domains and microservices, and the closed project API contract.
<!-- CONFIG-END -->

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-010 | Draft `notification-service.yaml` OpenAPI contract (+140 lines) | doing | https://github.com/code-corhuila/property-docs/commit/f94b50f32a44d729ba59ea8b786ed4767735e8a9 — PR #18 (opened 2026-09-21, closed unmerged and re-submitted in Week 9 as PR #31) |
| DOC-API-011 | Draft `payment-service.yaml` OpenAPI contract (+206 lines) | doing | PR #19 (opened 2026-09-21, closed unmerged and re-submitted in Week 9 as PR #33) — https://github.com/code-corhuila/property-docs/pull/19 |
| DOC-API-012 | API guidelines for domains and microservices (`07-api/guidelines.md`) and the project API contract (`07-api/api-contract.md`) | done | https://github.com/code-corhuila/property-docs/pull/26 (opened 2026-09-24, merged 2026-09-25) — my commits `0a35a1b`, `c5d1f26` |
| DOC-API-013 | Close the project API contract and remove obsolete authentication and reservation endpoints | done | https://github.com/code-corhuila/property-docs/pull/27 (opened 2026-09-24, merged 2026-09-25) — commits `3f2b552`, `19c0f0c` |

> **Evidence note:** PRs #18 and #19 were closed without merging because their content
> was reworked and re-submitted the following week; they are listed as `doing` for that
> reason. PRs #26 and #27 also contain commits by teammates (API errors and gaps); only
> the commits listed above are mine.

## 2. My individual contribution

- Wrote the first drafts of the **notification-service** and **payment-service** OpenAPI
  contracts (the services that had no contract yet), opened as PRs #18 and #19.
- Wrote the first version of the **API guidelines** for domains and microservices
  (`guidelines.md`) and of the **closed project API contract** (`api-contract.md`),
  and removed obsolete authentication and reservation endpoints from it; teammates added
  their own commits on top of the same PRs (error taxonomy, contract gaps).

## 3. Blockers and risks

- PRs #18 and #19 did not merge in this week; the work was delivered in Week 9.
- The guidelines and contract written this week were later rewritten against the course
  norm (single error envelope, integer cents), so they are not the final rules.
- One commit message is not a Conventional Commit (`Remove obsolete authentication and
  reservation endpoints`).

## 4. Plan for next week

- Re-submit the notification and payment contracts as clean PRs.
- Add the Booking and Catalog endpoint fiches and the sequence diagrams of the Saga.

## 5. Compliance self-check

- [ ] Conventional Commits - type(scope): summary — *(one message does not follow the format; see Blockers)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own `docs/` branch + PR to `main`)*
- [x] Testable acceptance criteria — *(each endpoint in the contracts can be validated against the OpenAPI schema and the domain events)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `07-api/contracts/openapi/notification-service.yaml`, `07-api/contracts/openapi/payment-service.yaml` (drafts)
- `07-api/guidelines.md`, `07-api/api-contract.md`
- https://github.com/code-corhuila/property-docs/pull/18, https://github.com/code-corhuila/property-docs/pull/19, https://github.com/code-corhuila/property-docs/pull/26, https://github.com/code-corhuila/property-docs/pull/27
- Repository: https://github.com/code-corhuila/property-docs
