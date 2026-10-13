<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       09-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 09

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Angie Valentina Florez Vargas
- GITHUB_USER: Angie20000
- TEAM: Pms_Property
- SPRINT_GOAL: Finish the API contract layer and align it with the course norm (single error envelope, money in integer cents), and record the architecture decisions the new topology needs — orchestrated Saga, hexagonal guide, price lookup from Booking to Catalog — plus the Catalog and Identity data models.
<!-- CONFIG-END -->

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-UML-004 | Sequence diagrams for authentication and the booking–payment Saga (login, Saga, expiry) | done | https://github.com/code-corhuila/property-docs/pull/29 (merged 2026-09-28) — commit `3cb4a55` |
| DOC-API-014 | `notification-service.yaml` OpenAPI contract (re-submitted after PR #18) | done | https://github.com/code-corhuila/property-docs/pull/31 (opened 2026-09-28) — commit `47f86ff` |
| DOC-API-015 | `payment-service.yaml` OpenAPI contract (re-submitted after PR #19) | done | https://github.com/code-corhuila/property-docs/pull/33 (opened 2026-09-28) — commit `19d1665` |
| DOC-API-016 | Booking and Catalog endpoint fiches | done | https://github.com/code-corhuila/property-docs/pull/35 (merged 2026-09-28) — commit `8813577` |
| DOC-API-017 | Propagate the single error envelope and integer cents to all 7 OpenAPI files | done | https://github.com/code-corhuila/property-docs/pull/39 (merged 2026-09-30) — commit `c551577` |
| DOC-ARCH-015 | ADR-009 (orchestrated Saga in `-workflow`) and ADR-010 (Saga state store); amend ADR-004 and ADR-007 | done | https://github.com/code-corhuila/property-docs/pull/47 (merged 2026-10-02) — commit `ad5462f` |
| DOC-ARCH-016 | Update `overview.md` and the communication matrix for the new topology | done | https://github.com/code-corhuila/property-docs/pull/61 (merged 2026-10-04) — commit `c6531e8` |
| DOC-ARCH-017 | Rewrite the hexagonal architecture guide for Java 21 and three modules | done | https://github.com/code-corhuila/property-docs/pull/63 (opened 2026-10-03, merged 2026-10-04) — commit `803e0f6` |
| DOC-ARCH-018 | ADR-015 — Booking reads the price from Catalog's API | done | https://github.com/code-corhuila/property-docs/pull/69 (merged 2026-10-04) — commit `e972c60` |
| DOC-DATA-003 | Rewrite the Catalog and Identity data models and the data dictionary | done | https://github.com/code-corhuila/property-docs/pull/71 (merged 2026-10-04) — commit `0925dbf` |

## 2. My individual contribution

- **Sequence diagrams.** Added `seq-auth-login`, `seq-saga-booking-payment` and
  `seq-saga-expiration` (Mermaid), so the Saga and the expiry flow can be followed
  step by step.
- **Service contracts.** Re-submitted the notification (+125 lines) and payment
  (+179 lines) OpenAPI files as clean PRs, and wrote the Booking (+140) and Catalog (+87)
  endpoint fiches.
- **Norm alignment.** Propagated the single error envelope and `montoCents` (integer
  cents) to `_shared.yaml` and the gateway, booking, catalog, identity, notification
  and payment OpenAPI files in one PR.
- **ADR-009 and ADR-010.** Recorded that the Saga runs orchestrated inside
  `property-workflow`, with a dedicated state store, and cross-referenced ADR-004 and
  ADR-007 so the earlier choreographed design is not silently contradicted.
- **Architecture documents.** Updated `overview.md` and the inter-service communication
  matrix for the new topology, and rewrote the hexagonal guide for Java 21 and three
  modules.
- **ADR-015.** Documented that Booking obtains the price from Catalog through its API,
  with the consequences for the other ADRs and the hexagonal guide.
- **Data models.** Rewrote the Catalog and Identity models and extended the data
  dictionary.
- Merged PRs #57 and #59 into `main`.

## 3. Blockers and risks

- ADR-009 changes how the Saga is described in earlier documents; the contracts and
  fiches written this week were adjusted again in Week 10 (see its report).
- PRs #31 and #33 are the re-submission of work first drafted in Week 8.
- No service code exists yet, so none of this can be verified by tests.

## 4. Plan for next week

- Translate the shared components and the service template to English.
- Add the workflow contract, the Saga rulebook and the corrected payment fiches.
- Align the requirements and the context diagrams with the new decisions.

## 5. Compliance self-check

- [x] Conventional Commits - type(scope): summary — *(e.g. `docs(architecture): add adr-009 and adr-010 for the orchestrated saga`)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own `docs/` branch + PR to `main`)*
- [x] Testable acceptance criteria — *(each ADR lists context, options, dominant criterion, accepted cost and consequences; contracts can be validated against the OpenAPI schema)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `08-uml/diagrams/source/seq-auth-login.md`, `seq-saga-booking-payment.md`, `seq-saga-expiration.md`
- `07-api/contracts/openapi/` — notification, payment, shared and all service files
- `07-api/contracts/booking-endpoint-fiches.md`, `catalog-endpoint-fiches.md`
- `05-architecture/decisions/records/ADR-009-orchestrated-saga-in-workflow.md`, `ADR-010-saga-state-store.md`, `ADR-015-booking-reads-catalog-api.md`
- `05-architecture/overview.md`, `inter-service-communication.md`, `hexagonal-architecture.md`
- `06-data/models.md`, `06-data/data-dictionary.md`
- https://github.com/code-corhuila/property-docs/pull/29, https://github.com/code-corhuila/property-docs/pull/31, https://github.com/code-corhuila/property-docs/pull/33, https://github.com/code-corhuila/property-docs/pull/35, https://github.com/code-corhuila/property-docs/pull/39, https://github.com/code-corhuila/property-docs/pull/47, https://github.com/code-corhuila/property-docs/pull/61, https://github.com/code-corhuila/property-docs/pull/63, https://github.com/code-corhuila/property-docs/pull/69, https://github.com/code-corhuila/property-docs/pull/71
- Repository: https://github.com/code-corhuila/property-docs
