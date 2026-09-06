package com.wikmind.service.common.entity;

import com.wikmind.service.common.entity.enums.OutboxAggregateType;
import com.wikmind.service.common.entity.enums.OutboxEventType;
import com.wikmind.service.common.entity.enums.OutboxStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Table(name = "outbox_events")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {
    @Id
    UUID id;


    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    OutboxEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 100)
    OutboxAggregateType aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    UUID aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    OutboxStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    private OutboxEvent(UUID id, OutboxEventType eventType, OutboxAggregateType aggregateType, UUID aggregateId, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
        this.attemptCount = 0;
    }

    public static OutboxEvent pending(UUID eventId, OutboxEventType eventType, OutboxAggregateType aggregateType, UUID aggregateId, String payload) {
        return new OutboxEvent(eventId, eventType, aggregateType, aggregateId, payload);
    }

    public void markPublished(Instant publishedAt) {
        if (this.status != OutboxStatus.PUBLISHING) {
            throw new IllegalStateException("Only PUBLISHING events can be marked as PUBLISHED");
        }

        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = publishedAt;
        this.lockedUntil = null;
        this.errorMessage = null;
    }

    public void markPublishing(Instant lockedUntil) {
        if (this.status != OutboxStatus.PENDING) {
            throw new IllegalStateException("Only PENDING events can be marked as PUBLISHING");
        }

        this.status = OutboxStatus.PUBLISHING;
        this.lockedUntil = lockedUntil;
        this.lastAttemptAt = Instant.now();
        this.attemptCount++;
    }

    public void markPublishFailed(String errorMessage, Instant nextAttemptAt) {
        if (this.status != OutboxStatus.PUBLISHING) {
            throw new IllegalStateException("Only PUBLISHING events can be marked as failed");
        }

        this.status = OutboxStatus.PENDING;
        this.lockedUntil = null;
        this.errorMessage = errorMessage;
        this.nextAttemptAt = nextAttemptAt;
    }

    public void recordAttempt(Instant attemptedAt, Instant nextAttemptAt, String errorMessage) {
        this.attemptCount++;
        this.lastAttemptAt = attemptedAt;
        this.nextAttemptAt = nextAttemptAt;
        this.errorMessage = errorMessage;
    }

    public void markFailed(String errorMessage) {
        this.status = OutboxStatus.FAILED;
        this.errorMessage = errorMessage;
    }

}
