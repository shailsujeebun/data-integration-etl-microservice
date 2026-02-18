# Build Guide - Data Integration & ETL Microservice

## 1. Build Objective

- **Narrative Goal:** Pipeline-centric platform focused on lineage, reliability, and transform transparency.
- **Primary Interaction Principle:** Pipeline run states and retries are visible with explicit failure diagnostics.
- **Hero/System Hook:** Data flow hero with source-to-target traceability.

## 2. Prerequisites

- Node.js 20.x LTS+ (for frontend/tooling scripts).
- Runtime SDK based on selected backend track (Java/.NET/Python/Go/Node).
- Docker + Docker Compose for local dependencies.
- Git and CI/CD platform access.

## 3. Initialization

```bash
# Generate backend with Spring Initializr (Web, Security, Data JPA, Validation)
```

## 4. Environment Template

Create `.env.local` and provider-specific secrets files:
```env
APP_ENV=development
APP_PORT=3000
API_PORT=8080
JWT_SECRET=replace_me
DATABASE_URL=postgresql://user:password@localhost:5432/app_db
```

## 5. Suggested Repository Structure

```text
data-integration-etl-microservice/
  apps/
    frontend/
    backend/
  infra/
    docker/
    iac/
  docs/
    architecture/
    api/
  tests/
    unit/
    integration/
    e2e/
  assets/
    images/  # see IMAGE_GUIDE.md
```

## 6. Implementation Phases

1. **Foundation:** bootstrap repositories, shared coding standards, initial CI checks.
2. **Core Domain:** implement baseline entities, APIs, and access-control model.
3. **Feature Delivery:** implement SRS features (The system shall be able to ingest data from external REST APIs (GET requests).; The system shall be able to parse CSV files from a valid local directory or S3 bucket.; The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records.; The extraction process must support incremental loading (fetching only new data since the last run).; The system shall validate incoming data against a predefined schema (e.g., data types, not null constraints).).
4. **Observability:** add logs, metrics, tracing, and audit trails.
5. **Hardening:** security tests, performance tests, and release validation.

## 7. Data Model and Contracts

| Entity | Baseline Fields | Notes |
|---|---|---|
| SourceConfig Table | `id, type (API/CSV/DB), connection_string, schedule_cron, last_run_timestamp` | Derived from SRS schema section |
| UnifiedData Table | `id, original_source_id, external_id, payload (JSONB), normalized_fields...` | Derived from SRS schema section |
| JobHistory Table | `id, job_name, status (SUCCESS/FAILED), records_processed, error_count, start_time, end_time` | Derived from SRS schema section |
| ErrorLog Table | `id, job_id, raw_data, error_reason` | Derived from SRS schema section |

## 8. Testing Strategy

- Unit tests for validators, policy logic, and transformations.
- Integration tests for API endpoints and persistence boundaries.
- E2E tests for top P0 role-based workflows.
- Load tests for critical throughput/latency targets from SRS.

## 9. CI/CD and Release

- Enforce lint, static analysis, tests, and security scan gates.
- Publish versioned artifacts with changelog and rollback metadata.
- Use staged environment promotion with approval checks where required.
- Validate telemetry and alert routes in first production hour.

## 10. Deliverable Traceability

- Map all expected deliverables from SRS chapter 6 to concrete repository outputs.

## 11. Distinctiveness Guardrails

- Keep this implementation domain-specific, not generic admin boilerplate.
- Ensure UI, diagrams, and docs directly reflect the project problem domain.
- Preserve project signature hook: Data flow hero with source-to-target traceability.

