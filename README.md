# Data Integration & ETL Microservice

[![CI](https://github.com/yourname/data-integration-etl-microservice/actions/workflows/ci.yml/badge.svg)](https://github.com/yourname/data-integration-etl-microservice/actions/workflows/ci.yml)
> Replace `yourname/data-integration-etl-microservice` with your actual GitHub repo path after pushing.

A production-grade ETL platform demonstrating enterprise data engineering patterns. Configurable multi-source ingestion, schema validation, idempotent upsert loading, and real-time observability via a React dashboard.

## Architecture

```
[Data Sources]          [ETL Engine]            [PostgreSQL]       [REST API]      [React UI]
REST APIs       →       Extract                 unified_data   →   /api/data   →   Dashboard
CSV Files       →       Transform    →    →     job_history        /api/jobs       Job Monitor
PostgreSQL DB   →       Load (Upsert)           error_log          /api/sources    Source Config
                        ↓
                   [Scheduler]
                   Cron / Manual Trigger
```

## Quick Start

```bash
git clone https://github.com/yourname/data-integration-etl-microservice
cd data-integration-etl-microservice
cp .env.example .env
docker-compose -f infra/docker/docker-compose.yml up --build
```

Wait ~60–90 seconds for the first build. Then:

- **Dashboard:** http://localhost:80
- **API health:** http://localhost:8080/actuator/health
- **API docs:** See `docs/API.md`
- **Railway deploy guide:** See `docs/RAILWAY_DEPLOY.md`

## Key Features

| Feature | Description |
|---------|-------------|
| Multi-source ingestion | REST APIs (with auth), CSV files, external PostgreSQL via JDBC |
| Schema validation | Required field checks; bad rows isolated to error log |
| Date normalization | Handles MM/DD/YYYY, DD-MM-YYYY, YYYY-MM-DD → ISO 8601 |
| Idempotent loading | Native SQL `ON CONFLICT DO UPDATE` — safe to re-run any job |
| Incremental loading | Tracks `last_run_timestamp` per source; appends `since=` to API requests |
| Partial failure | A single bad row does not stop the job — logged and skipped |
| Dynamic scheduling | Cron expressions stored in DB, registered with `ThreadPoolTaskScheduler` at runtime |
| Structured logging | JSON logs via Logstash encoder — ready for Splunk, ELK, CloudWatch |
| React dashboard | Pipeline run history, error log viewer, manual job trigger |

## Key Engineering Decisions

**Upsert strategy:** Uses native SQL `INSERT ... ON CONFLICT DO UPDATE` against a `UNIQUE(source_config_id, external_id)` constraint. This is the only approach that guarantees true idempotency at the database level — JPA find-or-create is not safe under concurrent execution.

**Batch performance:** `EntityManager.flush()` + `EntityManager.clear()` every 500 rows in `UpsertWriter`. Without `clear()`, Hibernate's first-level cache grows unbounded, causing OOM at 100k+ records.

**Dynamic scheduling:** `@Scheduled` is compile-time static and cannot be reconfigured at runtime. `ThreadPoolTaskScheduler` + `ConcurrentHashMap<Long, ScheduledFuture<?>>` allows per-source cron registration, cancellation, and re-registration when source config changes.

**JSONB payload column:** Source schemas are heterogeneous (every source has different fields). Storing the full normalized record as JSONB in `unified_data.payload` provides flexibility without schema migrations per source. Source-specific metadata is added as enrichment fields (`_source_name`, `_source_type`, `_ingested_at`).

**Error isolation:** Each record is transformed independently. A validation failure on row 4 of 1000 logs the error and continues — the job completes as `PARTIAL`, not `FAILED`.

## Project Structure

```
data-integration-etl-microservice/
├── backend/                    # Java 17 + Spring Boot 3
│   └── src/main/java/com/dataetl/
│       ├── pipeline/           # ETL engine (extract, transform, load)
│       ├── service/            # Job history, dynamic scheduler
│       ├── controller/         # REST API endpoints
│       ├── model/              # JPA entities
│       └── config/             # CORS, scheduler, WebClient config
├── frontend/                   # React 18 + TypeScript + Tailwind
│   └── src/
│       ├── pages/              # Dashboard, Sources, JobDetail
│       ├── components/         # Shared UI components
│       ├── hooks/              # useJobs (polling), useSources
│       └── api/                # Typed axios client
├── infra/docker/               # Dockerfile + docker-compose.yml
├── sample-data/                # sample.csv with intentional invalid row
└── docs/                       # API reference, architecture docs
```

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sources` | List all source configs |
| POST | `/api/sources` | Create new source |
| PUT | `/api/sources/{id}` | Update source (re-registers schedule) |
| DELETE | `/api/sources/{id}` | Delete source (cancels schedule) |
| GET | `/api/jobs` | Paginated job history |
| GET | `/api/jobs/{id}` | Job detail + error log |
| POST | `/api/jobs/trigger` | Manually trigger a job (async, returns 202) |
| GET | `/api/data` | Query processed data with filters |
| GET | `/actuator/health` | Health check |

## Source Configuration Examples

**CSV source:**
```json
{
  "name": "products-csv",
  "type": "CSV",
  "connectionString": "/app/sample-data/sample.csv",
  "scheduleCron": "0 0/30 * * * ?"
}
```

**REST API source (with bearer auth):**
```json
{
  "name": "orders-api",
  "type": "API",
  "connectionString": "https://api.example.com/orders",
  "authConfig": { "type": "bearer", "token": "your-token-here" },
  "scheduleCron": "0 0 * * * ?"
}
```

**External database source:**
```json
{
  "name": "crm-customers",
  "type": "DB",
  "connectionString": "jdbc:postgresql://crm-host:5432/crmdb?user=reader&password=pass",
  "authConfig": { "query": "SELECT id, name, email, created_at FROM customers" },
  "scheduleCron": "0 0 2 * * ?"
}
```

> **Note:** Spring cron has 6 fields (seconds included): `"0 0/5 * * * ?"` = every 5 minutes.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2, Spring Data JPA |
| Database | PostgreSQL 15, Flyway migrations |
| HTTP Client | Spring WebFlux WebClient (with retry) |
| CSV Parsing | OpenCSV 5.9 |
| JSONB Mapping | hypersistence-utils-hibernate-63 |
| Logging | Logback + logstash-logback-encoder (structured JSON) |
| Frontend | React 18, TypeScript, Vite, Tailwind CSS |
| HTTP | Axios with typed API client |
| Container | Docker, Docker Compose, Nginx |

## Performance Targets

- Process 100,000 records in under 5 minutes (batch size 500, flush/clear pattern)
- API response time under 200ms for indexed queries
- Partial failure resilience — bad rows logged, valid rows continue to load

## Recruiter Demo Mode

For recruiter walkthroughs, you can enable fast cron schedules and protected write operations.

1. Copy env file and set credentials:
```bash
cp .env.example .env
```
2. Edit `.env`:
```env
APP_ADMIN_USERNAME=etladmin
APP_ADMIN_PASSWORD=replace_with_strong_password
SPRING_PROFILES_ACTIVE=default,demo
```
3. Start containers:
```bash
docker compose -f infra/docker/docker-compose.yml up --build -d
```

With `demo` profile enabled:
- seed sources are upserted on startup
- schedules are shortened (`2`, `3`, and `5` minute intervals)
- `POST/PUT/DELETE` write endpoints require HTTP Basic auth

## Cross-Domain Frontend + API

If frontend and backend run on different domains:

- Set backend CORS origins in `.env`:
```env
APP_CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,https://admin.your-frontend-domain.com
```
- Set frontend API base URL at build time:
```env
VITE_API_BASE_URL=https://your-api-domain.com/api
```
