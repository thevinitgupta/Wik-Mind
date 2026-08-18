-- ============================================================
-- V6 - Create Outbox Events
-- ============================================================

CREATE TABLE outbox_events
(
    id              UUID PRIMARY KEY,

    event_type      VARCHAR(100)             NOT NULL,

    aggregate_type  VARCHAR(100)             NOT NULL,

    aggregate_id    UUID                     NOT NULL,

    payload         JSONB                    NOT NULL,

    status          VARCHAR(50)              NOT NULL DEFAULT 'PENDING',

    attempt_count   INTEGER                  NOT NULL DEFAULT 0,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,

    published_at    TIMESTAMP WITH TIME ZONE,

    last_attempt_at TIMESTAMP WITH TIME ZONE,

    next_attempt_at TIMESTAMP WITH TIME ZONE,

    error_message   TEXT,

    CONSTRAINT chk_outbox_attempt_count CHECK (attempt_count >= 0)
);


-- ============================================================
-- Indexes
-- ============================================================

CREATE INDEX idx_outbox_pending_events ON outbox_events (status, next_attempt_at, created_at);

CREATE INDEX idx_outbox_aggregate ON outbox_events (aggregate_type, aggregate_id);

CREATE INDEX idx_outbox_created_at ON outbox_events (created_at);