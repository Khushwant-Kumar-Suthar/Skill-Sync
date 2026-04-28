# SkillSync Backend

Spring Boot REST API for SkillSync (authentication, skill/DSA tracking, recommendations, and integrations).

## Tech stack

- **Java**: 21
- **Framework**: Spring Boot (Web, Security, Validation)
- **Persistence**: Spring Data JPA / Hibernate
- **Database**: MySQL
- **Auth**: JWT
- **Build**: Maven

## Prerequisites

- Java **21**
- Maven **3.9+**
- MySQL **8+**

## Configuration

### Server

- Default port: `8080`

### Database

Configured in `src/main/resources/application.properties`:

- URL: `jdbc:mysql://localhost:3306/skillsync_db`
- Username: `root`
- Password: `${DB_Password}` (environment variable)

Set the env var in your terminal session before running:

PowerShell:

```powershell
$env:DB_Password="your_mysql_password"
```

### JWT

`application.properties` contains:

- `jwt.secret`
- `jwt.expiration` (milliseconds)

For production, you should move the secret to an environment variable (and reference it in properties), e.g.:

- `JWT_SECRET`: strong secret (>= 32 chars for HS256)

## Run locally

From `skillsync-backend/`:

```bash
mvn spring-boot:run
```

API starts at `http://localhost:8080`.

## Profiles

There is a dev profile file: `src/main/resources/application-dev.properties`.

Run with the dev profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Build

```bash
mvn clean package
```

The packaged jar will be in `target/`.

## Tests

```bash
mvn test
```

## Logging

`application.properties` points to `logback-spring.xml`:

- `logging.config=classpath:logback-spring.xml`

If you need to change log levels, prefer editing `logback-spring.xml` (including per-profile `<springProfile>` blocks).

## Troubleshooting

- **MySQL connection errors**: ensure MySQL is running, `skillsync_db` exists, and `DB_Password` is set for the current shell session.
- **Port already in use**: stop the process using `8080` or change `server.port` in `application.properties`.
- **JWT errors**: ensure the configured secret is long enough and consistent across restarts if you rely on existing tokens.

