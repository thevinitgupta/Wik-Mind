CREATE TABLE sources (
                         id UUID PRIMARY KEY,

                         workspace_id UUID NOT NULL,

                         name VARCHAR(255) NOT NULL,

                         storage_key VARCHAR(1024),

                         type VARCHAR(50) NOT NULL,

                         status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

                         mime_type VARCHAR(255),

                         size BIGINT,

                         checksum VARCHAR(64) NOT NULL,

                         failure_reason TEXT,

                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                         updated_at TIMESTAMP WITH TIME ZONE,

                         CONSTRAINT fk_source_workspace
                             FOREIGN KEY (workspace_id)
                                 REFERENCES workspaces(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT chk_source_size
                             CHECK (size IS NULL OR size >= 0)
    );

CREATE INDEX idx_source_workspace
    ON sources(workspace_id);

CREATE INDEX idx_source_status
    ON sources(status);

CREATE INDEX idx_source_type
    ON sources(type);

CREATE INDEX idx_source_created_at
    ON sources(created_at DESC);

CREATE UNIQUE INDEX uk_source_workspace_checksum
    ON sources(workspace_id, checksum);