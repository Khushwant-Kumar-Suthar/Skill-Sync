# SkillSync

SkillSync is a full‑stack application for tracking developer skills and DSA progress, analyzing coding activity, and generating learning recommendations/roadmaps.

## Repository structure

- `skillsync-backend/`: Spring Boot REST API (Java, Maven, MySQL, JWT)
- `skillsync-frontend/`: React web app (Vite)

## Prerequisites

- **Backend**: Java **21**, Maven, MySQL 8+
- **Frontend**: Node.js **18+** (recommended), npm

## Quick start (local)

### 1) Start the backend

- Create a MySQL database named `skillsync_db`.
- Set the database password as an environment variable:
  - `DB_Password`: password for your MySQL user (default user in config is `root`)

Run:

```bash
cd skillsync-backend
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

### 2) Start the frontend

Run:

```bash
cd skillsync-frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:3000` and proxies API requests from `/api` to `http://localhost:8080`.

## Configuration overview

### Backend

- **Port**: `8080` (see `skillsync-backend/src/main/resources/application.properties`)
- **Database**
  - URL: `jdbc:mysql://localhost:3306/skillsync_db`
  - Username: `root`
  - Password: `${DB_Password}` (environment variable)
- **JWT**
  - Current config uses `jwt.secret` in properties for local development.
  - Recommended for production: move JWT secret to an environment variable (see backend README for details).

### Frontend

- **Dev server port**: `3000` (see `skillsync-frontend/vite.config.js`)
- **API proxy**: `/api` → `http://localhost:8080`

## Common scripts

### Frontend

- `npm run dev`: start dev server
- `npm run build`: production build
- `npm run preview`: preview production build locally

### Backend

- `mvn spring-boot:run`: run the API
- `mvn test`: run tests
- `mvn package`: build jar

## Troubleshooting

- **Frontend calls fail with 404/ECONNREFUSED**: ensure backend is running on `8080` and requests are prefixed with `/api`.
- **MySQL access denied**: confirm MySQL is running, the username matches `spring.datasource.username`, and `DB_Password` is set in your environment.
- **Java version mismatch**: ensure `java -version` reports Java 21.

## License

Add a license if you plan to open-source this project.

