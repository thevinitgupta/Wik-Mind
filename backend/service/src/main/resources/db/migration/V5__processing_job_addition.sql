CREATE TABLE processing_jobs
(
    id                UUID PRIMARY KEY,

    source_version_id UUID        NOT NULL,

    attempt_count     INTEGER     NOT NULL DEFAULT 0,

    processing_status VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    processing_stage  VARCHAR(50) NOT NULL DEFAULT 'EXTRACTION',

    created_at        TIMESTAMP WITH TIME ZONE,
    updated_at        TIMESTAMP WITH TIME ZONE,
    started_at        TIMESTAMP WITH TIME ZONE,
    completed_at      TIMESTAMP WITH TIME ZONE,

    error_message     TEXT,

    CONSTRAINT fk_processing_job_source_version FOREIGN KEY (source_version_id) REFERENCES source_versions (id),

    CONSTRAINT uk_processing_job_source_version UNIQUE (source_version_id)
)

