# RoleMate — Backend

RoleMate is a role-playing character manager with accounts, character sheets, and spell lookup. This repository contains the Java API used by the [RoleMate Frontend](https://github.com/Ugits/RoleMate-Frontend), a Next.js and TypeScript interface.

## Features

- Register users and authenticate with JWT bearer tokens.
- Create, list, retrieve, update, and delete characters belonging to the authenticated user.
- Store character names, levels, and six ability scores in PostgreSQL.
- Delete your own account; administrator endpoints also support administrator creation, account activation/deactivation, and user deletion.
- Retrieve spells by level through an external Spellify-compatible HTTP service.

## Stack and design

[build.gradle](build.gradle) defines Java 17, Spring Boot 3.4.0, Spring Security, Spring Data JPA, Jakarta Bean Validation, PostgreSQL, and JJWT 0.12.6. The included wrapper uses Gradle 8.11.1.

- **Feature packages:** `auth`, `user`, `character`, and `api` separate authentication, account operations, character management, and external-service integration. Controllers delegate to services and JPA repositories.
- **Ownership checks:** character services derive the current user from authentication and check ownership before individual reads, updates, or deletion. Users have a one-to-many relationship with characters; deleting a user cascades to their characters.
- **Authentication:** Spring Security uses stateless requests and a JWT filter. Passwords are hashed with BCrypt. `/admin/**` requires the ADMIN role; character endpoints require authentication.
- **External API boundary:** a Spring WebClient retrieves spell data and maps it to DTOs. Spell data is not persisted in this application's database.

See the [Java source](src/main/java/org/jonas/rolemate_backend) for the implementation.

## Run locally

### Requirements

- JDK 17.
- PostgreSQL with a database and credentials you control. No database version is pinned.
- A reachable Spellify-compatible service exposing `GET /usable/{level}` and returning records compatible with [SpellDTO](src/main/java/org/jonas/rolemate_backend/api/dto/SpellDTO.java).
- Network access for the Gradle wrapper and dependencies on first use.

The spell service URL and implementation are not supplied by these two repositories. A valid URL is required for configuration; a working service is required to retrieve spells.

### Configuration

```bash
git clone https://github.com/Ugits/RoleMate-Backend.git
cd RoleMate-Backend
```

Create the `src/main/resources` directory if needed, then create `application.properties` inside it. This file is excluded by [.gitignore](.gitignore). The following is a local-development example; replace the placeholders with your own values:

```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/rolemate
spring.datasource.username=YOUR_DATABASE_USER
spring.datasource.password=YOUR_DATABASE_PASSWORD
spring.jpa.hibernate.ddl-auto=update
SPELLIFY_API_URL=https://YOUR_SPELL_SERVICE_BASE_URL
```

Create the database first. The example's `ddl-auto=update` lets Hibernate create or update tables for local development; the repository contains no database migration scripts. Keep credentials local.

`SPELLIFY_API_URL` is read by [ApiWebClient](src/main/java/org/jonas/rolemate_backend/api/config/ApiWebClient.java). The service appends `/usable/{level}` to this base URL. Spring Boot also accepts environment variables for configuration, such as `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SPRING_JPA_HIBERNATE_DDL_AUTO`, and `SPELLIFY_API_URL`.

Start the API:

```bash
# macOS / Linux
./gradlew bootRun
```

```powershell
# Windows PowerShell
.\gradlew.bat bootRun
```

With the example configuration, the API uses `http://localhost:8080`. Configure the frontend's `BASE_URL` to match. [CorsConfig](src/main/java/org/jonas/rolemate_backend/config/security/CorsConfig.java) permits `http://localhost:3000`; the spell controller also declares that origin.

## API example

These Bash/curl requests follow the current controllers and DTOs. They are illustrative and have not been executed as part of this documentation update.

Register a local example user, then sign in:

```bash
curl -i -X POST http://localhost:8080/user/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo-user","password":"local-demo-password"}'

curl -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo-user","password":"local-demo-password"}'
```

Registration requires a username of 4–20 characters and a password of 7–36 characters. Login returns `token` and `role`. Copy the returned token into the local shell variable below:

```bash
TOKEN='PASTE_RETURNED_TOKEN_HERE'

curl -X POST http://localhost:8080/character/create \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Arin","level":3,"strength":10,"dexterity":14,"constitution":12,"intelligence":16,"wisdom":13,"charisma":11}'

curl http://localhost:8080/character/fetch-all \
  -H "Authorization: Bearer $TOKEN"
```

Character creation returns a character DTO with its generated `id`. The owner comes from the authenticated user.

| Method | Path | Access / request |
| --- | --- | --- |
| POST | `/user/register` | Public; username and password |
| POST | `/auth/login` | Public; username and password |
| GET | `/character/fetch-all` | Authenticated user's characters |
| POST | `/character/create` | Authenticated; character fields |
| POST | `/character/fetch` | Owner; `{"id": 1}` |
| PUT | `/character/update` | Owner; full character DTO including ID |
| DELETE | `/character/delete` | Owner; `{"id": 1}`; returns 204 |
| DELETE | `/user/delete-me` | Authenticated user's account |
| POST | `/admin/register` | ADMIN; username and password |
| PATCH | `/admin/user/status` | ADMIN; `{"username":"demo-user","isEnabled":false}` |
| DELETE | `/admin/user/delete` | ADMIN; username |
| POST | `/api/spell/level` | Public; `{"level": 3}`; requires the external service |

## Tests and build

```bash
./gradlew test
./gradlew build
```

On Windows, use `.\gradlew.bat test` and `.\gradlew.bat build`.

The [test source](src/test/java/org/jonas/rolemate_backend) contains a Spring context-load test and MockMvc tests for successful character creation, retrieval, update, and deletion. They load the application context and use repositories, so supply configuration and a dedicated local test database. There is no committed test profile or embedded database dependency.

These commands and test descriptions were checked against the source. No passing test run, build, or complete application run is claimed by this documentation update.

## Current scope

- Local application configuration, database provisioning, and the external spell service must be supplied separately.
- The first administrator must be provisioned separately: public registration creates a USER, while administrator registration already requires ADMIN access. No administrator bootstrap is committed.
- JWTs expire after seven days, but the signing key is generated anew at startup, invalidating existing tokens after a restart. There is no refresh-token endpoint.
- Validation is incomplete: character update uses `@Valid`, while character creation does not. Frontend creation limits and backend DTO limits differ.
- Authentication currently logs generated tokens, and registration returns a credentials DTO containing the password hash. These behaviors need revision before deployment.

RoleMate is under active development, with the current setup focused on local use.
