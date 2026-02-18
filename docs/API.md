# API Reference

Base URL: `http://localhost:8080` (direct) or `http://localhost:80/api/` (via Nginx)

All responses are JSON. Timestamps are ISO 8601 with timezone offset.

---

## Authentication

Write operations are protected with HTTP Basic Auth.

- Required credentials come from backend environment variables:
  - `APP_ADMIN_USERNAME`
  - `APP_ADMIN_PASSWORD`
- Protected endpoints:
  - `POST /api/sources`
  - `PUT /api/sources/{id}`
  - `DELETE /api/sources/{id}`
  - `POST /api/jobs/trigger`
- Read-only endpoints (`GET /api/**`) remain public for demo viewing.

---

## Sources

### GET /api/sources
List all source configurations.

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "sample-products-csv",
    "type": "CSV",
    "connectionString": "/app/sample-data/sample.csv",
    "hasAuthConfig": false,
    "scheduleCron": "0 0/30 * * * ?",
    "lastRunTimestamp": "2024-01-20T14:30:00Z",
    "isActive": true,
    "createdAt": "2024-01-15T10:00:00Z"
  }
]
```

### POST /api/sources
Create a new source configuration.

**Request body:**
```json
{
  "name": "my-api-source",
  "type": "API",
  "connectionString": "https://api.example.com/data",
  "authConfig": { "type": "bearer", "token": "abc123" },
  "scheduleCron": "0 0 * * * ?",
  "isActive": true
}
```

**Response 201:** Same shape as GET source response.

**Validation errors 400:**
```json
{ "errors": { "name": "Name is required", "type": "Type is required (API, CSV, or DB)" } }
```

### PUT /api/sources/{id}
Update an existing source. Automatically re-registers the scheduler if cron changes.

### DELETE /api/sources/{id}
Delete a source. Cancels any scheduled jobs for this source.

---

## Jobs

### POST /api/jobs/trigger
Manually trigger an ETL job. Returns immediately (async execution).

**Request body:**
```json
{ "sourceConfigId": 1 }
```

**Response 202:**
```json
{
  "message": "Job triggered successfully",
  "sourceConfigId": 1,
  "status": "RUNNING"
}
```

### GET /api/jobs
Paginated job history, newest first.

**Query params:** `page` (default 0), `size` (default 20)

**Response 200:**
```json
{
  "content": [
    {
      "id": 42,
      "sourceConfigId": 1,
      "sourceName": "sample-products-csv",
      "jobName": "etl-sample-products-csv-1705756200000",
      "status": "PARTIAL",
      "recordsProcessed": 7,
      "errorCount": 1,
      "startTime": "2024-01-20T14:30:00Z",
      "endTime": "2024-01-20T14:30:02Z",
      "triggeredBy": "MANUAL",
      "durationMs": 1842
    }
  ],
  "totalElements": 42,
  "totalPages": 3,
  "page": 0,
  "size": 20
}
```

**Job status values:** `RUNNING`, `SUCCESS`, `FAILED`, `PARTIAL`
**Trigger type values:** `SCHEDULER`, `MANUAL`

### GET /api/jobs/{id}
Job detail with error log.

**Response 200:**
```json
{
  "job": { /* same as above */ },
  "errors": [
    {
      "id": 7,
      "rawData": "{id=, name=, category=Books, amount=, created_date=bad-date, email=, status=}",
      "errorReason": "Missing required identifier field (id, _id, uuid, or record_id)",
      "occurredAt": "2024-01-20T14:30:01Z"
    }
  ]
}
```

---

## Data

### GET /api/data
Query normalized data from the unified store.

**Query params:**
- `sourceType` — filter by source type: `API`, `CSV`, or `DB`
- `from` — ISO 8601 start timestamp (e.g., `2024-01-01T00:00:00Z`)
- `to` — ISO 8601 end timestamp
- `page` — page number (default 0)
- `size` — page size (default 20)

**Example:** `GET /api/data?sourceType=CSV&from=2024-01-01T00:00:00Z&page=0&size=10`

**Response 200:**
```json
{
  "content": [
    {
      "id": 12,
      "sourceConfigId": 1,
      "externalId": "1",
      "sourceType": "CSV",
      "sourceName": "sample-products-csv",
      "payload": {
        "id": "1",
        "name": "Alice Johnson",
        "category": "Electronics",
        "amount": "299.99",
        "created_date": "2024-01-15",
        "_source_name": "sample-products-csv",
        "_source_type": "CSV",
        "_ingested_at": "2024-01-20T14:30:01Z"
      },
      "ingestedAt": "2024-01-20T14:30:01Z",
      "updatedAt": "2024-01-20T14:30:01Z"
    }
  ],
  "totalElements": 7,
  "totalPages": 1,
  "page": 0,
  "size": 20
}
```

---

## Health

### GET /actuator/health
Spring Boot Actuator health check.

**Response 200:**
```json
{
  "status": "UP"
}
```
