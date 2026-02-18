# Data Integration & ETL Microservice

[![CI](https://github.com/shailsujeebun/data-integration-etl-microservice/actions/workflows/ci.yml/badge.svg)](https://github.com/shailsujeebun/data-integration-etl-microservice/actions/workflows/ci.yml)

Production-style ETL platform with multi-source ingestion, transformation, idempotent upsert loading, dynamic scheduling, and a React operations dashboard.

## 60-Second Pitch

- Ingests from API, CSV, and external PostgreSQL sources.
- Normalizes and validates records with partial-failure handling.
- Loads into a unified PostgreSQL model with idempotent `ON CONFLICT DO UPDATE` writes.
- Tracks run history, errors, and status in a live dashboard.
- Supports cron scheduling and manual triggers.

## Local Run (Fast Path)

```bash
git clone https://github.com/shailsujeebun/data-integration-etl-microservice
cd data-integration-etl-microservice
cp .env.example .env
docker compose -f infra/docker/docker-compose.yml up --build
```

Open:

- Dashboard: `http://localhost`
- API health: `http://localhost:8080/actuator/health`
- API docs: `docs/API.md`

## Recruiter Demo Mode

Use faster scheduler intervals and protected write actions:

```env
APP_ADMIN_USERNAME=etladmin
APP_ADMIN_PASSWORD=replace_with_strong_password
SPRING_PROFILES_ACTIVE=default,demo
```

Run with:

```bash
docker compose -f infra/docker/docker-compose.yml up --build -d
```

In demo mode:

- Seed sources are upserted at startup.
- Schedules run every 2, 3, and 5 minutes.
- Write endpoints require HTTP Basic Auth.

## Screenshots

Screenshot placeholders are prepared in `assets/screenshots/README.md`.

Recommended captures:

- `assets/screenshots/dashboard.png`
- `assets/screenshots/sources.png`
- `assets/screenshots/job-detail.png`
- `assets/screenshots/trigger-flow.png`

## Architecture

```text
[Data Sources] --> [Extract] --> [Transform] --> [Load/Upsert] --> [PostgreSQL]
      |                                                         |
      +-------------------------- Scheduler ---------------------+

[React Dashboard] <--> [REST API] <--> [Job History + Error Log + Unified Data]
```

## Key Engineering Decisions

- Idempotency at DB layer: native `INSERT ... ON CONFLICT DO UPDATE` on `UNIQUE(source_config_id, external_id)`.
- Memory-safe batching: periodic `flush()` + `clear()` while loading large datasets.
- Runtime scheduling: `ThreadPoolTaskScheduler` + per-source registration/cancellation.
- Error isolation: bad records are logged and skipped; jobs can complete as `PARTIAL`.
- Security hardening: SSRF guards, SQL query restrictions, input validation, actuator minimization, auth-secret redaction.

## API Snapshot

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/sources` | List source configs |
| POST | `/api/sources` | Create source (admin auth) |
| PUT | `/api/sources/{id}` | Update source (admin auth) |
| DELETE | `/api/sources/{id}` | Delete source (admin auth) |
| GET | `/api/jobs` | Paginated run history |
| GET | `/api/jobs/{id}` | Run detail + row-level errors |
| POST | `/api/jobs/trigger` | Trigger ETL run (admin auth) |
| GET | `/api/data` | Query unified records |

## Deployment

- Railway guide: `docs/RAILWAY_DEPLOY.md`
- Docker compose stack: `infra/docker/docker-compose.yml`

## Tech Stack

- Backend: Java 17, Spring Boot 3.2, Spring Data JPA, Flyway
- Database: PostgreSQL 15
- Frontend: React 18, TypeScript, Vite, Tailwind CSS
- Infra: Docker, Nginx, Railway-compatible Dockerfiles/config
