# notification-at-scale

A hands-on system design learning project built with Spring Boot 4.x. The same application grows to cover different system design topics over time — the current focus is **notification at scale**.

The base is a LinkedIn-style social platform: users, posts, follows, likes, and comments. On top of that sits an event-driven notification system (transactional and promotional) powered by Kafka. Future iterations may introduce other system design concerns (rate limiting, search, feed ranking, caching strategies, etc.) on the same codebase.

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

## Jira

- **Site:** bouaro.atlassian.net
- **Project:** NOTIFICATION (`NOTIFY`)
- **Ticket format:** `NOTIFY-<number>`

## Commit style

Enforced by the `commit-msg` hook. Do not include co-author trailers.

### Format

```
type(TICKET-ID): short description
```

### Types and ticket rules

| Type | Ticket | Example |
|---|---|---|
| `feat` | required | `feat(NOTIFY-12): add email notification channel` |
| `fix` | required | `fix(NOTIFY-7): correct null check in dispatcher` |
| `refactor` | required | `refactor(NOTIFY-3): extract notification factory` |
| `test` | required | `test(NOTIFY-9): add unit tests for event handler` |
| `chore` | optional | `chore: configure Checkstyle and PMD` |
| `docs` | optional | `docs(NOTIFY-2): update architecture overview` |
| `style` | optional | `style: reformat import order` |

Ticket ID format: uppercase letters, a dash, digits — e.g. `NOTIFY-42`.

Freeform exceptions: `initial commit`, `initial setup` (case-insensitive).
