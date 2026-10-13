<!-- Your weekly grade is read AUTOMATICALLY from this file:
   10-week/hu-status/README.md  (inside YOUR fork). English. -->
# Weekly Status - Week 10

FULL_NAME: Angie Valentina Florez Vargas
GITHUB_USER: Angie20000
TEAM: Pms_Property
SPRINT_GOAL: Bring the contracts, requirements and diagrams in line with the accepted
architecture (orchestrated Saga, ADR-015) and start the web application: the container
that mounts the portals, the catalog and booking portals, and the CI that guards them.

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-018 | Translate the shared OpenAPI components and the service template to English | done | https://github.com/code-corhuila/property-docs/pull/91 (merged 2026-10-05) — commit `e93d369` |
| DOC-API-019 | Add the workflow contract (`workflow.yaml`) for the "reservar" Saga | done | https://github.com/code-corhuila/property-docs/pull/93 (merged 2026-10-05) — commit `a7281b3` |
| DOC-API-020 | Align the Catalog contract and the Booking fiches with ADR-015 | done | https://github.com/code-corhuila/property-docs/pull/95 (merged 2026-10-05) — commit `9069fa5` |
| DOC-API-021 | Saga rulebook, workflow fiches and rewritten payment fiches | done | https://github.com/code-corhuila/property-docs/pull/104 (merged 2026-10-05) — commit `c69cbd6` |
| DOC-REQ-001 | Add identity and notification user stories | done | https://github.com/code-corhuila/property-docs/pull/118 (merged 2026-10-06) — commit `7fd7cd2` |
| DOC-REQ-002 | Align traceability matrix, non-functional requirements and product vision | done | https://github.com/code-corhuila/property-docs/pull/120 (merged 2026-10-06) — commit `e70ca84` |
| DOC-UML-005 | Redraw the context, container and context-map diagrams | done | https://github.com/code-corhuila/property-docs/pull/122 (merged 2026-10-06) — commit `b6f545d` |
| DOC-PROD-001 | Start cut 2 of the product with the web application (product backlog) | done | https://github.com/code-corhuila/property-docs/pull/130 (merged 2026-10-06) — commit `b3b1920` |
| FRONT-001 | `property-front`: mount the portals and isolate one that is down (federation manifest, nginx, compose, `remote-unavailable` component with tests, +262 lines) | done | https://github.com/code-corhuila/property-front/pull/5 (opened 2026-10-07) — commit `f6fc68f` |
| FRONT-002 | `property-front`: run the CI only on `develop` and `qa`, ten minutes at most | done | https://github.com/code-corhuila/property-front/pull/8 (develop) and https://github.com/code-corhuila/property-front/pull/10 (promotion to `qa`) — commits `12ff2f6`, `0d8cf58` |
| CAT-001 | `property-catalog-portal`: list the properties with their four states (property card and explore page, with tests, +321 lines) | done | https://github.com/code-corhuila/property-catalog-portal/pull/3 (opened 2026-10-08) — commit `eb70222` |
| BOOK-001 | `property-booking-portal`: contract types, API calls and mock API (+391 lines) | done | https://github.com/code-corhuila/property-booking-portal/pull/2 (opened 2026-10-09) — commit `67c0f9b` |
| BOOK-002 | `property-booking-portal`: list my bookings by tab (page and tests, +287 lines) | done | https://github.com/code-corhuila/property-booking-portal/pull/5 (opened 2026-10-10) — commit `879a915` |
| PLAT-001 | Promote work of teammates from `develop` to `qa` with pull requests | done | https://github.com/code-corhuila/property-front/pull/11 and https://github.com/code-corhuila/property-catalog-portal/pull/7 — commits are authored by teammates; I opened and carried the promotion |

## 2. My individual contribution

- **Contracts.** Translated the shared OpenAPI components and the service template,
  wrote the `workflow.yaml` contract (+262 lines) for the Saga, aligned the Catalog
  contract and Booking fiches with ADR-015, and added the Saga rulebook and the workflow
  fiches (+195 lines) while rewriting the payment fiches.
- **Requirements and product.** Added the identity and notification user stories (+219
  lines), aligned the traceability matrix, the non-functional requirements and the
  product vision, and opened cut 2 of the backlog with the web application.
- **Diagrams.** Redrew the C1 system context, the C2 container diagram and the context
  map so they match the accepted architecture.
- **Code (new this week).** Built the container piece that mounts the portals and keeps
  the app usable when one of them is down; the list of properties with their four states
  in the catalog portal; the booking portal's contract types, API calls and mock API, and
  its "my bookings" page with tabs; and limited the CI to `develop` and `qa` with a
  ten-minute cap. The new components and services ship with unit tests.
- Carried teammates' changes from `develop` to `qa` through promotion PRs, which are
  listed separately because the commits are theirs.
- Merged PRs #85, #87, #89 and #103 into `main` of `property-docs`.

## 3. Blockers and risks

- Backend and database are not required for this cut (professor); the portals work
  against a mock API, so the real contracts are not exercised end to end yet.
- The week is still open at the time of writing (2026-10-11); anything merged after that
  is not included.
- The contracts must stay in sync with ADR-009 and ADR-015 while the services are not
  implemented.

## 4. Plan for next week

- Keep the OpenAPI files and fiches consistent with any change in the Saga or in the
  gateway routes.
- Replace the mock API of the booking portal with the real contract once it exists.

## 5. Compliance self-check

- [x] Conventional Commits - type(scope): summary — *(e.g. `docs(api): add workflow contract for the saga reservar`)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own `docs/` branch + PR to `main`)*
- [x] Testable acceptance criteria — *(user stories carry acceptance criteria and are traced in the traceability matrix; contracts can be validated against the OpenAPI schema)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `07-api/contracts/openapi/workflow.yaml`, `_shared.yaml`, `_template-service.yaml`, `catalog-service.yaml`
- `07-api/contracts/booking-endpoint-fiches.md`, `payment-endpoint-fiches.md`, `workflow-endpoint-fiches.md`
- `04-requirements/user-stories.md`, `traceability-matrix.md`, `non-functional.md`
- `08-uml/diagrams/source/c1-system-context.md`, `c2-container-diagram.md`, `ctx-domain-map.md`
- `03-product/product-backlog.md`
- Code repositories: property-front, property-catalog-portal, property-booking-portal (PRs listed in section 1)
- https://github.com/code-corhuila/property-docs/pull/91, https://github.com/code-corhuila/property-docs/pull/93, https://github.com/code-corhuila/property-docs/pull/95, https://github.com/code-corhuila/property-docs/pull/104, https://github.com/code-corhuila/property-docs/pull/118, https://github.com/code-corhuila/property-docs/pull/120, https://github.com/code-corhuila/property-docs/pull/122, https://github.com/code-corhuila/property-docs/pull/130
- Repository: https://github.com/code-corhuila/property-docs
