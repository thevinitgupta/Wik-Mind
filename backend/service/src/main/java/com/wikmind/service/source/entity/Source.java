package com.wikmind.service.source.entity;

import com.wikmind.service.source.entity.enums.SourceStatus;
import com.wikmind.service.source.entity.enums.SourceType;
import com.wikmind.service.workspace.entity.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.MimeType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "sources",
        indexes = {
                @Index(name = "idx_source_workspace", columnList = "workspace_id"),
                @Index(name = "idx_source_status", columnList = "status"),
                @Index(name = "idx_source_type", columnList = "type"),
                @Index(name = "idx_source_created_at", columnList = "created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_source_workspace_checksum",
                        columnNames = {"workspace_id", "checksum"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Source {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    /**
     * Original filename or display name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Location inside object storage.
     */
    @Column(name = "storage_key")
    private String storageKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceStatus status = SourceStatus.PENDING;

    @Column(name = "mime_type")
    private String mimeType;

    private Long size;

    @Column(nullable = false, length = 64)
    private String checksum;

    @Column(name = "failure_reason")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Factory method for new uploads.
     */
    public static Source create(
            Workspace workspace,
            String name,
            SourceType type,
            String checksum
    ) {

        Source source = new Source();
        source.workspace = workspace;
        source.name = name;
        source.type = type;
        source.status = SourceStatus.PENDING;
        source.id = UUID.randomUUID();
        source.checksum = checksum;

        return source;
    }

    public void markProcessing() {

        this.status = SourceStatus.PROCESSING;
        this.failureReason = null;
    }

    public void markReady() {

        this.status = SourceStatus.READY;
        this.failureReason = null;
    }

    public void markFailed(String reason) {

        this.status = SourceStatus.FAILED;
        this.failureReason = reason;
    }

    public void markDeleted() {

        this.status = SourceStatus.DELETED;
    }

    public void completeUpload(
            String storageKey,
            String mimeType,
            long size
    ) {
        this.storageKey = storageKey;
        this.mimeType = mimeType;
        this.size = size;
    }
}