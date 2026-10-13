<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       07-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 07

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Pms_Property
- SPRINT_GOAL: Learn and exercise the team's pull-request flow on the documentation repository before sending the real API contracts.
<!-- CONFIG-END -->

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-GOV-004 | Practice PR: add a point to `03-product/README.md` to exercise branch, PR, review and merge | done | https://github.com/code-corhuila/property-docs/pull/6 (opened 2026-09-17, merged 2026-09-22) — commits `a35d58b`, `055e17b` |

> **Evidence note:** the PR was opened inside Week 7 and merged on 2026-09-22, after the
> week closed; the merge date is shown instead of hidden.

## 2. My individual contribution

- Opened a practice pull request (#6) with a one-line change in the product README, to
  learn the branch → PR → review → merge flow the team uses before sending real
  contract work. It is a test change by design and is listed as such.

## 3. Blockers and risks

- This week's output is small: only the practice PR. The real API work (notification
  and payment contracts) started on 2026-09-21 and is reported in Week 8.
- Two commit messages are not Conventional Commits (`Task completed` and
  `feat(docs): add a point` for a docs-only change).

## 4. Plan for next week

- Draft the OpenAPI contracts of `notification-service` and `payment-service`.
- Write the API guidelines for domains and microservices.

## 5. Compliance self-check

- [ ] Conventional Commits - type(scope): summary — *(see Blockers: two messages need a fix)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own `docs/` branch + PR to `main`)*
- [x] Testable acceptance criteria — *(the change is a single line that can be checked in the README)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- https://github.com/code-corhuila/property-docs/pull/6
- Repository: https://github.com/code-corhuila/property-docs
