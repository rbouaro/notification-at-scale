Validate the current implementation against the acceptance criteria of Jira ticket $ARGUMENTS.

Steps:
1. Fetch the ticket from the NOTIFY project (cloudId: 10c36cea-3e65-4c45-a685-2cc535be6893) and extract the acceptance criteria.
2. For each acceptance criterion, check whether it is satisfied by the current code:
   - Search for relevant entities, endpoints, constraints, and tests.
   - Mark each AC as PASS, FAIL, or PARTIAL with a one-line reason.
3. Check the Definition of Done from `.claude/scope.md`:
   - Tests present and passing (`./mvnw test`)?
   - `./mvnw verify` passes (static analysis)?
   - OpenAPI annotations on new endpoints?
   - Commit message will reference the ticket?
4. Produce a summary table: AC checklist + DoD checklist.
5. If any item is FAIL or PARTIAL, describe specifically what is missing.
