# Functional Requirements - Data Integration & ETL Microservice

## 1. Source and Scope

- **Source Document:** `2_Data_Integration_ETL_Microservice_SRS.md`
- **Project:** Data Integration & ETL Microservice
- **Purpose Summary:** The purpose of this document is to define the requirements for a backend Data Integration and ETL (Extract, Transform, Load) Microservice. This service is designed to ingest data from heterogeneous sources, normalize it, and expose it via a unified REST API, demonstrating strong backend and data engineering capabilities.
- **Solution Direction:** Pipeline-centric platform focused on lineage, reliability, and transform transparency.

## 2. User Roles

| Role | Responsibilities | Access Expectations |
|---|---|---|
| Administrator | Configures and governs the platform. | UI and API access must be permission-scoped and auditable |
| Operator | Executes day-to-day workflows. | UI and API access must be permission-scoped and auditable |
| Viewer | Observes reports and status with read access. | UI and API access must be permission-scoped and auditable |

## 3. Functional Requirement Matrix

| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| REQ-EXT-01 | The system shall be able to ingest data from external REST APIs (GET requests). | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-EXT-02 | The system shall be able to parse CSV files from a valid local directory or S3 bucket. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-EXT-03 | The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-EXT-04 | The extraction process must support incremental loading (fetching only new data since the last run). | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-TRANS-01 | The system shall validate incoming data against a predefined schema (e.g., data types, not null constraints). | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-TRANS-02 | Invalid records shall be logged to an error table/file and excluded from the main processing flow. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-TRANS-03 | The system shall normalize format differences (e.g., date formats `MM/DD/YYYY` vs `YYYY-MM-DD` coverage). | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-TRANS-04 | The system shall support basic data enrichment (e.g., calculating total fields or looking up reference IDs). | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-LOAD-01 | Transformed data shall be persisted to a PostgreSQL database. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-LOAD-02 | The loading process must be idempotent; restarting a failed job should not duplicate data. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-LOAD-03 | The system shall support "Upsert" (Update if exists, Insert if new) logic to maintain data consistency. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-API-01 | The system shall expose a REST API to query processed data. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-API-02 | The API shall support filtering by date range and source type. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-API-03 | The API shall provide paginated responses. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-SCHED-01 | The system shall include a scheduler (e.g., Quartz, Spring Scheduler) to trigger ETL jobs at configurable intervals. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |
| REQ-SCHED-02 | Admins shall be able to trigger a job manually via an API endpoint. | P0 | Requirement is implemented, testable, and mapped to at least one QA case |

## 4. Required User Journeys

1. **Initial Orientation:** User lands on entry route and reaches primary workflow in <=30 seconds.
2. **Core Execution:** User completes one high-value path (The system shall be able to ingest data from external REST APIs (GET requests). / The system shall be able to parse CSV files from a valid local directory or S3 bucket.) end-to-end.
3. **Failure Recovery:** User can recover from a known validation/network failure without restarting session.
4. **Return Continuity:** User can return to prior context (state/filter/draft) across navigation or refresh.

## 5. Domain Entities and Data Baseline

| Entity | Purpose | Minimum Fields |
|---|---|---|
| SourceConfig Table | SRS-defined domain model | `id, type (API/CSV/DB), connection_string, schedule_cron, last_run_timestamp` |
| UnifiedData Table | SRS-defined domain model | `id, original_source_id, external_id, payload (JSONB), normalized_fields...` |
| JobHistory Table | SRS-defined domain model | `id, job_name, status (SUCCESS/FAILED), records_processed, error_count, start_time, end_time` |
| ErrorLog Table | SRS-defined domain model | `id, job_id, raw_data, error_reason` |

## 6. Integration and Interface Requirements

- **Language:** Java 17+ (Spring Boot 3) or C# (.NET 8).
- **Persistence:** PostgreSQL 15+.
- **Migration Tool:** Flyway or Liquibase.

## 7. Functional QA Gate

- Every requirement ID must map to a test case or validation checklist item.
- All P0 flows must have explicit loading, error, and recovery behavior.
- Access control boundaries must be validated for each role and protected endpoint.

