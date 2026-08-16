package com.wikmind.service.source.entity;

import com.wikmind.service.source.entity.enums.ProcessingStage;
import com.wikmind.service.source.entity.enums.ProcessingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Table(name = "processing_jobs", uniqueConstraints = {@UniqueConstraint(name = "uk_processing_job_source_version", columnNames = "source_version_id")})
@Entity
@Getter
public class ProcessingJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_version_id", nullable = false)
    private SourceVersion sourceVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false)
    private ProcessingStatus processingStatus = ProcessingStatus.QUEUED;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_stage", nullable = false)
    private ProcessingStage processingStage = ProcessingStage.EXTRACTION;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public static ProcessingJob queued(SourceVersion sourceVersion){
        ProcessingJob job = new ProcessingJob();
        job.sourceVersion = sourceVersion;
        job.processingStatus = ProcessingStatus.QUEUED;
        job.processingStage = ProcessingStage.EXTRACTION;
        return job;
    }
}
