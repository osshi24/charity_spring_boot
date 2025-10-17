# Repository Guidelines

## Project Structure & Modules
- Source: `src/main/java/com/example/charitybe` → layers: `Controllers`, `Services`, `Repositories`, `Entites` (entities), `dto`, `Config`, `exceptions`, `enums`, `mapper`, `Services/blockchain`.
- Resources: `src/main/resources` (e.g., `application.properties`, `static/swagger/index.html`).
- Tests: `src/test/java/com/example/charitybe`.
- Build & runtime: Maven (Java 17), Dockerfile, `compose.yaml` (Postgres service).

## Build, Test, and Run
- **Java Version**: Java 21 (LTS) - ensure JAVA_HOME points to JDK 21
- Build JAR: `./mvnw clean package -DskipTests` (Windows: `.\mvnw.cmd clean package -DskipTests`).
- Run locally: `./mvnw spring-boot:run` (defaults to port `5555`).
- Tests: `./mvnw test`.
- Docker (app): `docker build -t charity-be . && docker run -p 5555:5555 charity-be`.
- Docker (DB): `docker compose up -d postgres` (adjust app datasource if using local DB).

## Coding Style & Conventions
- Java 21 (LTS), Spring Boot 3.5.6. Use 4-space indentation.
- Prefer constructor injection; use Lombok (`@RequiredArgsConstructor`, `@Getter/@Setter`, `@Data`) to reduce boilerplate.
- Naming: classes `PascalCase`; entities singular; DTOs suffixed `DTO`; controllers `*Controller`, services `*Service`/`*ServiceImpl`, repositories `*Repository`.
- Keep packages under `com.example.charitybe` and follow existing folder layout.

## Testing Guidelines
- Framework: JUnit 5 via `spring-boot-starter-test`.
- Location: mirror main packages under `src/test/java`.
- Naming: `*Tests.java` (e.g., `UserServiceTests.java`).
- Run: `./mvnw test`. Add focused tests for services and controller endpoints (MockMvc/WebTest).

## Commit & Pull Requests
- Commits: imperative present (“Add login validation”), concise subject, optional body for context; group related changes.
- Reference issues (e.g., `#123`) when applicable.
- PRs: clear description, what/why, test instructions, and any API changes (consider updating `static/swagger` if relevant).

## Security & Configuration
- Do not commit secrets. Prefer env vars to override properties: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `BLOCKCHAIN_*`.
- Defaults point to remote DB; for local dev, override datasource or add `application-local.properties` and run with `-Dspring-boot.run.profiles=local`.
- CORS is configured in `src/main/java/com/example/charitybe/Config/CorsConfig.java`; restrict origins for production.
- Blockchain is optional (`blockchain.enabled=false`); configure RPC and keys only via secure secrets management.

