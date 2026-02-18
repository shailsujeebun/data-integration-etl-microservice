-- =============================================================================
-- V1: Initial Schema for Data Integration & ETL Microservice
-- =============================================================================

-- Source configuration: defines where to pull data from
CREATE TABLE source_config (
    id                      BIGSERIAL PRIMARY KEY,
    name                    VARCHAR(255) NOT NULL UNIQUE,
    type                    VARCHAR(10) NOT NULL CHECK (type IN ('API', 'CSV', 'DB')),
    connection_string       TEXT NOT NULL,
    auth_config             JSONB,
    schedule_cron           VARCHAR(100),
    last_run_timestamp      TIMESTAMP WITH TIME ZONE,
    is_active               BOOLEAN NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Unified normalized data: destination for all ETL-processed records
CREATE TABLE unified_data (
    id                  BIGSERIAL PRIMARY KEY,
    source_config_id    BIGINT REFERENCES source_config(id) ON DELETE SET NULL,
    external_id         VARCHAR(512) NOT NULL,
    source_type         VARCHAR(10) NOT NULL CHECK (source_type IN ('API', 'CSV', 'DB')),
    source_name         VARCHAR(255),
    payload             JSONB NOT NULL,
    ingested_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    -- Unique constraint drives upsert idempotency (ON CONFLICT DO UPDATE)
    CONSTRAINT uq_unified_data UNIQUE (source_config_id, external_id)
);

-- Job execution history: one row per ETL run
CREATE TABLE job_history (
    id                  BIGSERIAL PRIMARY KEY,
    source_config_id    BIGINT REFERENCES source_config(id) ON DELETE SET NULL,
    job_name            VARCHAR(255),
    status              VARCHAR(10) NOT NULL CHECK (status IN ('RUNNING', 'SUCCESS', 'FAILED', 'PARTIAL')),
    records_processed   INT NOT NULL DEFAULT 0,
    error_count         INT NOT NULL DEFAULT 0,
    start_time          TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    end_time            TIMESTAMP WITH TIME ZONE,
    triggered_by        VARCHAR(10) NOT NULL CHECK (triggered_by IN ('SCHEDULER', 'MANUAL'))
);

-- Row-level error log: bad records that failed validation/transformation
CREATE TABLE error_log (
    id              BIGSERIAL PRIMARY KEY,
    job_id          BIGINT REFERENCES job_history(id) ON DELETE CASCADE,
    raw_data        TEXT,
    error_reason    TEXT,
    occurred_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Performance indexes
CREATE INDEX idx_unified_data_source_config   ON unified_data(source_config_id);
CREATE INDEX idx_unified_data_ingested_at     ON unified_data(ingested_at DESC);
CREATE INDEX idx_unified_data_source_type     ON unified_data(source_type);
CREATE INDEX idx_job_history_source_config    ON job_history(source_config_id);
CREATE INDEX idx_job_history_status           ON job_history(status);
CREATE INDEX idx_job_history_start_time       ON job_history(start_time DESC);
CREATE INDEX idx_error_log_job_id             ON error_log(job_id);
