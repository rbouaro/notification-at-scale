# notification-at-scale

Spring Boot 4.x event-driven notification service using PostgreSQL, Kafka, and Spring Modulith.

## Tech stack

- Java 26, Spring Boot 4.1.1, Spring Modulith
- PostgreSQL 18 (HikariCP pool)
- Apache Kafka (KRaft, single broker)
- Thymeleaf, Spring HATEOAS, WebSocket
- Lombok

## Running locally

All infrastructure runs via Docker Compose. Copy `.env` and adjust values as needed, then:

```bash
docker compose up -d
./mvnw spring-boot:run
```

Services:
- App: http://localhost:8088
- Kafka UI: http://localhost:8090
- PostgreSQL: localhost:5439

## Configuration

| File | Purpose |
|---|---|
| `application.yaml` | Base config: pool, timezone, Jackson, logging |
| `application-dev.yaml` | Dev overrides: datasource URL, Kafka, SQL logging, actuator |
| `.env` | Local env vars imported by both Spring and Compose |

Active profile is driven by `SPRING_PROFILES_ACTIVE` in `.env` (defaults to `dev`).

## Commit style

Enforced by the `commit-msg` hook. Do not include co-author trailers.

### Format

```
type(TICKET-ID): short description
```

### Types and ticket rules

| Type | Ticket | Example |
|---|---|---|
| `feat` | required | `feat(NAS-12): add email notification channel` |
| `fix` | required | `fix(NAS-7): correct null check in dispatcher` |
| `refactor` | required | `refactor(NAS-3): extract notification factory` |
| `test` | required | `test(NAS-9): add unit tests for event handler` |
| `chore` | optional | `chore: configure Checkstyle and PMD` |
| `docs` | optional | `docs(NAS-2): update architecture overview` |
| `style` | optional | `style: reformat import order` |

Ticket ID format: uppercase letters, a dash, digits — e.g. `NAS-42`.

Freeform exceptions: `initial commit`, `initial setup` (case-insensitive).
