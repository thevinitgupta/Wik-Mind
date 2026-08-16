-- ============================================================
-- V4 - Introduce Source Versions
-- ============================================================


-- ============================================================
-- 1. Create source_versions table
-- ============================================================

CREATE TABLE source_versions
(
    id             UUID PRIMARY KEY,

    source_id      UUID                     NOT NULL,

    version_number INTEGER                  NOT NULL,

    name           VARCHAR(255)             NOT NULL,

    storage_key    VARCHAR(1024),

    status         VARCHAR(50)              NOT NULL DEFAULT 'PENDING',

    mime_type      VARCHAR(255),

    size           BIGINT,

    checksum       VARCHAR(64)              NOT NULL,

    failure_reason TEXT,

    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_source_version_source FOREIGN KEY (source_id) REFERENCES sources (id) ON DELETE CASCADE,

    CONSTRAINT chk_source_version_number CHECK (version_number > 0),

    CONSTRAINT chk_source_version_size CHECK (size IS NULL OR size >= 0
) );


-- ============================================================
-- 2. Add latest version reference to sources
-- ============================================================

ALTER TABLE sources
    ADD COLUMN latest_version_id UUID;


-- ============================================================
-- 3. Remove version-specific columns from sources
-- ============================================================

ALTER TABLE sources
DROP COLUMN IF EXISTS storage_key,
DROP COLUMN IF EXISTS status,
DROP COLUMN IF EXISTS mime_type,
DROP COLUMN IF EXISTS size,
DROP COLUMN IF EXISTS checksum,
DROP COLUMN IF EXISTS failure_reason;


-- ============================================================
-- 4. Foreign key from Source -> latest SourceVersion
-- ============================================================

ALTER TABLE sources
    ADD CONSTRAINT fk_source_latest_version FOREIGN KEY (latest_version_id) REFERENCES source_versions (id) ON DELETE SET NULL;


-- ============================================================
-- 5. Indexes
-- ============================================================

CREATE INDEX idx_source_version_source ON source_versions (source_id);

CREATE INDEX idx_source_version_status ON source_versions (status);

CREATE INDEX idx_source_version_created_at ON source_versions (created_at DESC);

CREATE INDEX idx_source_latest_version ON sources (latest_version_id);


-- ============================================================
-- 6. Prevent duplicate version numbers within a Source
-- ============================================================

CREATE UNIQUE INDEX uk_source_version_source_version ON source_versions (source_id, version_number);


-- ============================================================
-- 7. Prevent duplicate content within the same Source
-- ============================================================

CREATE UNIQUE INDEX uk_source_version_source_checksum ON source_versions (source_id, checksum);