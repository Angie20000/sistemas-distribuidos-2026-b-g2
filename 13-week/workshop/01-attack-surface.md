# CP1 — Attack surface of Property

Sources: property-docs 05-architecture/overview.md, 05-architecture/deployment.md and the
OpenAPI contracts in 07-api/contracts/. My boundary, in red: **booking-api -> PostgreSQL (schema booking)**.

```mermaid
flowchart TB
  subgraph TB1["TB1 Untrusted clients"]
      user([Guest / Host / Admin])
      front[property-front + 5 portals]
  end
  subgraph TB2["TB2 Services in network platform"]
      gw[property-api-gateway :8080]
      wf[property-workflow :3006]
      booking[property-booking-api :3001]
      catalog[property-catalog-api :3002]
      payment[property-payment-api :3003]
      identity[property-identity-api :3004]
      notif[property-notification-api :3005]
      worker[property-worker]
      mq[[RabbitMQ]]
  end
  subgraph TB3["TB3 Shared database instances"]
      pg[(PostgreSQL 16: booking, payment, identity, notification, workflow)]
      mongo[(MongoDB 7: catalog)]
  end
    stripe[(Stripe sandbox)]
  user --> front
  front -->|HTTPS| gw
  gw -->|/api/v1/sagas| wf
  gw -->|/register /login /refresh /jwks| identity
  gw --> booking
  gw --> catalog
  gw --> payment
  gw --> notif
  wf -->|crear-reserva / confirmar| booking
  wf -->|cobrar / reembolso| payment
  payment -->|charge, refund| stripe
  worker -->|expire| booking
  booking -->|outbox| mq
  payment -->|outbox| mq
  mq --> notif
  mq --> catalog
  booking -->|booking_app| pg
  payment -->|payment_app| pg
  identity -->|identity_app| pg
  notif -->|notification_app| pg
  wf -->|workflow_app| pg
  catalog -->|catalog_app| mongo
  linkStyle 16 stroke:#d00,stroke-width:4px
```

## Assets of my boundary

| Asset | Where it lives | Classification |
|---|---|---|
| reservation rows (guest_id, property_id, dates, total_amount_cents, status) | booking.reservation | confidential |
| Password of booking_app | env/<environment>.env of property-infra-postgres (not versioned) | restricted |
| outbox_event payloads (ReservaCreada, ReservaConfirmada...) | booking.outbox_event | internal |
| idempotency keys of the Saga (<sagaId>:crear-reserva) | booking.idempotency_key | internal |
| Admin credential used by booking-db-migrate | env file of the runner (PG_ADMIN_USER, PG_ADMIN_PASSWORD) | restricted |

## Entry points of my boundary

| Entry point | Who calls it | Authentication today |
|---|---|---|
| postgres:5432 / database property | booking-api as booking_app | password from env; role booking_writer |
| postgres:5432, Liquibase runner | booking-db-migrate (runs once and exits) | instance admin; control tables databasechangelog_booking |
| Port 5432 in the network platform | any container in platform | not published to the host; password per user |
