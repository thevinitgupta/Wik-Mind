package com.wikmind.service.source.entity;

import com.wikmind.service.source.entity.enums.SourceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "source_versions",
        indexes = {
                @Index(
                        name = "idx_source_version_source",
                        columnList = "source_id"
                ),
                @Index(
                        name = "idx_source_version_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_source_version_created_at",
                        columnList = "created_at"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_source_version_source_version",
                        columnNames = {"source_id", "version_number"}
                ),
                @UniqueConstraint(
                        name = "uk_source_version_source_checksum",
                        columnNames = {"source_id", "checksum"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SourceVersion {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    /**
     * Original filename or display name for this version.
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
    private SourceStatus status = SourceStatus.PENDING;

    @Column(name = "mime_type")
    private String mimeType;

    private Long size;

    @Column(nullable = false, length = 64)
    private String checksum;

    @Column(name = "failure_reason")
    private String failureReason;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    public static SourceVersion create(
            Source source,
            int versionNumber,
            String name,
            String checksum
    ) {
        SourceVersion version = new SourceVersion();

        version.id = UUID.randomUUID();
        version.source = source;
        version.versionNumber = versionNumber;
        version.name = name;
        version.checksum = checksum;
        version.status = SourceStatus.PENDING;

        return version;
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

    public void completeUpload(
            String storageKey,
            String mimeType,
            long size
    ) {
        this.storageKey = storageKey;
        this.mimeType = mimeType;
        this.size = size;
        this.status = SourceStatus.UPLOADED;
    }
}