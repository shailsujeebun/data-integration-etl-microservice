# System Requirement Specification (SRS)
## Project: Data Integration & ETL Microservice

---

## 1. Introduction

### 1.1 Purpose
The purpose of this document is to define the requirements for a backend Data Integration and ETL (Extract, Transform, Load) Microservice. This service is designed to ingest data from heterogeneous sources, normalize it, and expose it via a unified REST API, demonstrating strong backend and data engineering capabilities.

### 1.2 Scope
The microservice will:
-   **Extract** data from REST APIs, CSV files, and relational databases.
-   **Transform** the raw data (cleaning, validation, normalization).
-   **Load** the processed data into a centralized PostgreSQL database.
-   Provide a REST API for consuming the processed data.
-   Include a scheduling mechanism for batch jobs.

### 1.3 Definitions, Acronyms, and Abbreviations
-   **ETL**: Extract, Transform, Load
-   **API**: Application Programming Interface
-   **CSV**: Comma-Separated Values
-   **cron**: Time-based job scheduler
-   **Idempotency**: The property of certain operations that they can be applied multiple times without changing the result beyond the initial application.

---

## 2. Overall Description

### 2.1 Product Perspective
This system acts as a middleware or data hub. It sits between various data providers (external APIs, legacy DBs, file drops) and downstream consumers (reporting tools, other internal services). It mimics enterprise-grade middleware solutions like Talend or MuleSoft but implemented as a custom lightweight microservice.

### 2.2 Operational Environment
-   **Backend**: Java (Spring Boot) or .NET Core.
-   **Database**: PostgreSQL.
-   **Deployment**: Docker container or standard server deployment.

---

## 3. Functional Requirements

### 3.1 Data Ingestion (Extract)
-   **REQ-EXT-01**: The system shall be able to ingest data from external REST APIs (GET requests).
-   **REQ-EXT-02**: The system shall be able to parse CSV files from a valid local directory or S3 bucket.
-   **REQ-EXT-03**: The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records.
-   **REQ-EXT-04**: The extraction process must support incremental loading (fetching only new data since the last run).

### 3.2 Data Transformation
-   **REQ-TRANS-01**: The system shall validate incoming data against a predefined schema (e.g., data types, not null constraints).
-   **REQ-TRANS-02**: Invalid records shall be logged to an error table/file and excluded from the main processing flow.
-   **REQ-TRANS-03**: The system shall normalize format differences (e.g., date formats `MM/DD/YYYY` vs `YYYY-MM-DD` coverage).
-   **REQ-TRANS-04**: The system shall support basic data enrichment (e.g., calculating total fields or looking up reference IDs).

### 3.3 Data Loading
-   **REQ-LOAD-01**: Transformed data shall be persisted to a PostgreSQL database.
-   **REQ-LOAD-02**: The loading process must be idempotent; restarting a failed job should not duplicate data.
-   **REQ-LOAD-03**: The system shall support "Upsert" (Update if exists, Insert if new) logic to maintain data consistency.

### 3.4 API Access
-   **REQ-API-01**: The system shall expose a REST API to query processed data.
-   **REQ-API-02**: The API shall support filtering by date range and source type.
-   **REQ-API-03**: The API shall provide paginated responses.

### 3.5 Scheduling & Orchestration
-   **REQ-SCHED-01**: The system shall include a scheduler (e.g., Quartz, Spring Scheduler) to trigger ETL jobs at configurable intervals.
-   **REQ-SCHED-02**: Admins shall be able to trigger a job manually via an API endpoint.

---

## 4. Non-Functional Requirements

### 4.1 Reliability & Fault Tolerance
-   **REQ-REL-01**: If an external source is unavailable, the job shall retry a configurable number of times before marking as failed.
-   **REQ-REL-02**: Partial failures (e.g., 10 bad rows in a 10,000 row CSV) should not crash the entire job; valid rows must still be processed.

### 4.2 Performance
-   **REQ-PERF-01**: The system shall be capable of processing 100,000 records in under 5 minutes.
-   **REQ-PERF-02**: API read response time shall be under 200ms for indexed queries.

### 4.3 Observability
-   **REQ-LOG-01**: All job runs must generate a structured log entry (Start Time, End Time, Records Processed, Records Failed, Status).
-   **REQ-LOG-02**: Logs should be output in JSON format for easy ingestion by log aggregators.

---

## 5. System Architecture

### 5.1 Technology Stack
-   **Language**: Java 17+ (Spring Boot 3) or C# (.NET 8).
-   **Persistence**: PostgreSQL 15+.
-   **Migration Tool**: Flyway or Liquibase.

### 5.2 Database Schema
*   **SourceConfig Table**: `id, type (API/CSV/DB), connection_string, schedule_cron, last_run_timestamp`
*   **UnifiedData Table**: `id, original_source_id, external_id, payload (JSONB), normalized_fields...`
*   **JobHistory Table**: `id, job_name, status (SUCCESS/FAILED), records_processed, error_count, start_time, end_time`
*   **ErrorLog Table**: `id, job_id, raw_data, error_reason`

### 5.3 Data Flow
1.  **Scheduler** triggers Job.
2.  **Reader** fetches data from Source.
3.  **Processor** validates and normalizes data.
4.  **Writer** upserts data into `UnifiedData` and logs errors to `ErrorLog`.
5.  **API Controller** queries `UnifiedData` to serve client requests.

---

## 6. Deliverables
1.  **Source Code**: Full backend application code.
2.  **DDL Scripts**: SQL to create the database schema.
3.  **Postman Collection**: Examples for triggering jobs and querying data.
4.  **Sample Data**: Dummy CSVs and SQL inserts to test the loop.
5.  **Documentation**: README explaining how to add a new data source.
