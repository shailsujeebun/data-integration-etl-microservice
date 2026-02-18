# Improvements Tracker

Status legend: ⬜ pending · 🔄 in progress · ✅ fixed

---

## 🔴 Critical

| # | Issue | Status |
|---|-------|--------|
| 1 | **PARTIAL demo broken** — row 4 of sample.csv had `id=4` so validation passed and the job always returned SUCCESS. Fixed by making row 4 have a blank `id` field (`,CORRUPTED_RECORD,Books,,bad-date,,`) — now the validator correctly rejects it, producing `PARTIAL` status with 7 processed + 1 error. | ✅ |
| 2 | **`CompletableFuture.runAsync()` uses `ForkJoinPool.commonPool()`** — shared JVM pool, slow jobs starve other async work. Fixed by exposing the `ThreadPoolTaskScheduler`'s underlying executor as a `@Bean("jobExecutor")` in `SchedulerConfig` and injecting it into `JobController` via `@Qualifier`. Manual triggers and scheduled jobs now share the same bounded `etl-scheduler-*` pool. | ✅ |

---

## 🟡 Bugs / Data Correctness

| # | Issue | Status |
|---|-------|--------|
| 3 | **`PUT /api/sources/{id}` does NOT re-register the scheduler** — `SourceController.updateSource()` calls `schedulerService.scheduleSource(saved)` ✅ already correct — confirmed in source. | ✅ |
| 4 | **Concurrent duplicate job runs** — nothing prevented two `RUNNING` jobs for the same source simultaneously. Fixed by adding an early-return guard in `EtlOrchestrator.runJob()` that queries `findTopBySourceConfigIdAndStatusOrderByStartTimeDesc(id, RUNNING)` before calling `startJob()`. Logs `job_skipped` with `already_running` reason. | ✅ |
| 5 | **`GlobalExceptionHandler` missing `DataIntegrityViolationException`** — duplicate source name gave raw 500 instead of 409 Conflict. Fixed by adding `@ExceptionHandler(DataIntegrityViolationException.class)` that inspects `getMostSpecificCause()` and returns 409 with a human-readable message. | ✅ |

---

## 🟠 Missing Features

| # | Issue | Status |
|---|-------|--------|
| 6 | **Scheduler seed crons never fire in demo** — seed crons are `0 0/30 * * * ?` (every 30 min) and `0 0 * * * ?` / `0 30 * * * ?` (hourly). Too slow to demo. Note: crons are real and correct — just not fast enough to show during a demo. Not a bug, just a UX note. | ✅ (by design) |

---

## 🟢 Code Quality / Frontend

| # | Issue | Status |
|---|-------|--------|
| 7 | **`useJobs` polls unconditionally every 3s** — even when all jobs are in terminal state. Fixed with adaptive polling: 3s interval while any job is `RUNNING`, drops to 30s when all jobs are terminal. `refetch` resets the timer instantly (used by trigger buttons). | ✅ |
| 8 | **`Sources.tsx` has no loading or error state** — confirmed already implemented: spinner on lines 15-19, coral error banner on lines 21-25, empty state on lines 27-31. No change needed. | ✅ |
| 9 | **`JobDetail.tsx` `rawData` null guard** — `{err.rawData && (...)}` is already guarded ✅. Confirmed in source. | ✅ |

---

## Fix Log

### Fix #1 — 2026-02-18
`sample-data/sample.csv` row 4 changed from `4,,Books,,bad-date,,` to `,CORRUPTED_RECORD,Books,,bad-date,,`. Blank `id` field now causes `TransformPipeline.validate()` to correctly reject the record with "Missing required identifier field". CSV job now produces `PARTIAL` (7 processed, 1 error) instead of `SUCCESS`.

### Fix #2 — 2026-02-18
`SchedulerConfig.java`: changed return type of `taskScheduler()` bean from `TaskScheduler` to `ThreadPoolTaskScheduler` (concrete type needed to call `getScheduledExecutor()`). Added `@Bean("jobExecutor") Executor jobExecutor(ThreadPoolTaskScheduler)` which returns the scheduler's underlying `ScheduledExecutorService`.
`JobController.java`: added `@Qualifier("jobExecutor") Executor jobExecutor` constructor parameter. `CompletableFuture.runAsync()` now passes `jobExecutor` as the second argument — runs on `etl-scheduler-*` threads instead of `ForkJoinPool.commonPool()`.

### Fix #4 — 2026-02-18
`EtlOrchestrator.java`: added `JobHistoryRepository` as a constructor dependency. Added early-return guard at the top of `runJob()` that calls `findTopBySourceConfigIdAndStatusOrderByStartTimeDesc(sourceConfigId, RUNNING)` — if a RUNNING job exists for the same source, logs `job_skipped` and returns immediately without creating a new job record.

### Fix #5 — 2026-02-18
`GlobalExceptionHandler.java`: added `import org.springframework.dao.DataIntegrityViolationException` and a new `@ExceptionHandler(DataIntegrityViolationException.class)` method. Inspects `ex.getMostSpecificCause().getMessage()` — if it contains "unique", returns a clean "already exists" message; otherwise returns a generic data integrity message. Both cases return HTTP 409 Conflict instead of 500.

### Fix #7 — 2026-02-18
`useJobs.ts`: rewrote polling logic. Added `TERMINAL_STATUSES` set and `hasRunningJob()` helper. Replaced static `setInterval` with adaptive `schedulePoll()` that checks the latest data after each fetch and sets interval to `activePollMs` (3s) if any job is RUNNING, or `idlePollMs` (30s) otherwise. Used `useRef` to hold the interval ID for reliable cleanup. `refetch` now calls `fetchJobs()` then `schedulePoll()` so trigger buttons immediately reset the active polling window.

---

## Verification — 2026-02-18

All fixes confirmed against running containers after rebuild:

| Fix | Verification |
|-----|-------------|
| #1 PARTIAL demo | `POST /api/jobs/trigger {sourceConfigId:1}` → job #12: `status:PARTIAL, recordsProcessed:7, errorCount:1`. Error log shows `rawData:{id=, name=CORRUPTED_RECORD,...}` and `errorReason:"Missing required identifier field"` ✅ |
| #2 Executor pool | `JobController` injects `@Qualifier("jobExecutor")` — manual triggers run on `etl-scheduler-*` thread pool, not `ForkJoinPool.commonPool()` ✅ |
| #4 Duplicate job guard | `EtlOrchestrator.runJob()` checks `findTopBySourceConfigIdAndStatusOrderByStartTimeDesc(id, RUNNING)` before starting. Concurrent triggers are skipped with `job_skipped` log event ✅ |
| #5 409 on duplicate name | `POST /api/sources` with existing name → `{"error":"A record with that value already exists (unique constraint violated)"}` HTTP 409 ✅ |
| #7 Smart polling | `useJobs` polls at 3s when RUNNING jobs present, 30s when all terminal. `refetch` resets timer on trigger ✅ |

---

## 🔐 Security Vulnerabilities

| # | Vulnerability | Severity | Status |
|---|---------------|----------|--------|
| V1 | **`authConfig` tokens returned in `GET /api/sources`** — bearer tokens and API keys were serialised into every sources response, visible to any visitor. Removed `authConfig` from `SourceConfigResponse`; replaced with `hasAuthConfig: boolean` (UI lock icon only). | 🔴 Critical | ✅ |
| V2 | **Stored SQL injection via `DbExtractor`** — `authConfig.query` was executed verbatim with no validation. Added SELECT-only enforcement and semicolon rejection before execution. Timestamp append now uses a bound `PreparedStatement` parameter instead of string concatenation. | 🔴 Critical | ✅ |
| V3 | **SSRF via `ApiExtractor` and `CsvExtractor`** — `connectionString` was fetched/opened with no URL validation; attackers could reach internal network services or traverse the filesystem. Created `SsrfGuard.java` with RFC-1918 blocklist, DNS resolution check, and file path prefix enforcement (`/app/sample-data/` only). Guard is called at **both** save time (`SourceController`) and extraction time (extractors) — malicious URLs are rejected before storage. | 🔴 Critical | ✅ |
| V4 | **`/actuator` proxied publicly through Nginx** — exposed system internals (DB status, disk path, hostname). Replaced the proxy block with `return 404`. Health checks run directly on the backend container via Docker Compose. | 🟡 High | ✅ |
| V5 | **Hardcoded credential fallbacks in `application.yml`** — `${DB_PASSWORD:etlpass}` meant the app silently used known-default creds if env vars weren't set. Removed all fallback values; app now fails fast at startup if env vars are missing. `docker-compose.yml` updated to pass all vars from `.env`. Actuator `show-details` changed from `always` to `never`; `metrics` and `info` endpoints removed. | 🟡 High | ✅ |
| V6 | **Invalid cron expressions saved without validation** — bad crons stored in DB only failed at runtime inside `DynamicSchedulerService`. Added `CronExpression.isValidExpression()` check in `SourceController` for both POST and PUT; returns 400 with an explanation before saving. | 🟡 High | ✅ |
| V7 | **`hashCode()` fallback ID in `UpsertWriter`** — Java's 32-bit `hashCode` could silently collide, causing records to overwrite each other. Replaced with `UUID.randomUUID()`. Fallback records now always insert rather than upsert (correct — without an ID we can't identify equality). | 🟡 Medium | ✅ |
| V8 | **Uncapped `?size=` parameter** — `GET /api/data?size=1000000` would load the entire table into memory. Added `MAX_PAGE_SIZE = 100` clamp in both `DataController` and `JobController`. | 🟢 Low | ✅ |
| V9 | **Full URI logged in `ApiExtractor`** — query params (which may include `?api_key=...`) were written to logs in plaintext. Changed to log only `host` + `path`, never query params. | 🟢 Low | ✅ |
| V10 | **Missing HTTP security headers** — no `X-Content-Type-Options`, `X-Frame-Options`, `Content-Security-Policy`, etc. Added all standard security headers in `nginx.conf` via `add_header ... always`. | 🟢 Low | ✅ |
