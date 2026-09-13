# Project Scope & Implementation Guidelines

## Purpose

This is a system design learning project. The codebase grows incrementally to explore different system design topics. The current focus is **notification at scale** on top of a LinkedIn-style social platform.

## Module structure (Spring Modulith)

Each domain is a self-contained Spring Modulith module under `com.rbouaro.notificationatscale`:

| Module | Responsibility |
|---|---|
| `user` | User entity, profile, registration |
| `auth` | JWT issuance, refresh, Spring Security config |
| `social` | Posts, follows, likes, comments |
| `notification` | Notification domain model, inbox, preferences |
| `feed` | Notification event production via Kafka |

Modules must NOT import internal types from other modules — use events or public APIs only.

## Definition of Done

Every story is done when ALL of the following are true:

- [ ] All acceptance criteria on the Jira ticket are met
- [ ] Unit tests written and passing
- [ ] `./mvnw verify` passes (Checkstyle + PMD + SpotBugs + JaCoCo)
- [ ] OpenAPI (`@Operation`, `@ApiResponse`) annotations on every new endpoint
- [ ] Commit message references the Jira ticket: `feat(NOTIFY-X): ...`
- [ ] No co-author trailer in commits

## Coding conventions

- Entities use a numeric PK (`id`, `@GeneratedValue(IDENTITY)`) for DB indexing and a UUID field (`uuid`, unique, non-updatable) as the client-facing identifier
- `uuid` is mapped to `id` in API responses via MapStruct so clients always see a UUID `id`
- All timestamps are `Instant` (UTC) — never `LocalDateTime`
- Pagination uses Spring Data `Pageable` with a default page size of 20
- Error responses follow RFC 9457 Problem Details (`ProblemDetail`); enable via `spring.mvc.problemdetails.enabled: true`
- Validation uses Jakarta Bean Validation (`@NotBlank`, `@Email`, etc.) on request DTOs
- Lombok is allowed on all classes: entities use `@Getter` + field-level `@Setter` only on mutable fields + `@NoArgsConstructor(access = PROTECTED)` for JPA; DTOs and services use `@Data` / `@RequiredArgsConstructor` as needed
- Use MapStruct (`@Mapper(componentModel = "spring")`) for entity → DTO mapping; Lombok must be listed before MapStruct in annotation processor paths
- API documentation interfaces live in a `doc` sub-package of each module (e.g. `user.doc.UserApi`); controllers implement these interfaces and carry zero Swagger annotations
- Infrastructure configuration (OpenAPI, Security, etc.) lives in the `config` package

## Ticket workflow

1. Pick a ticket from the NOTIFY backlog
2. Run `/work-on NOTIFY-X` to load the ticket context
3. Implement against the acceptance criteria
4. Run `/validate-ticket NOTIFY-X` before committing to self-check coverage
5. Commit with `feat(NOTIFY-X): short description`
