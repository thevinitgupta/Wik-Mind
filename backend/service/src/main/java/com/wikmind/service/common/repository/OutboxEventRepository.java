package com.wikmind.service.common.repository;

import com.wikmind.service.common.entity.OutboxEvent;
import com.wikmind.service.common.entity.enums.OutboxEventType;
import com.wikmind.service.common.entity.enums.OutboxStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    @Query(value = """
             SELECT *
              FROM outbox_events
              WHERE status = 'PENDING'
                AND event_type = 'PROCESSING_JOB_QUEUED'
                AND (
                    next_attempt_at IS NULL
                    OR next_attempt_at <= CURRENT_TIMESTAMP
                )
              ORDER BY created_at ASC
              LIMIT :batchSize
              FOR UPDATE SKIP LOCKED;
            """, nativeQuery = true)
    List<OutboxEvent> findPendingProcessingJobEvents(@Param("eventType") OutboxEventType eventType, @Param("status") OutboxStatus status, @Param("batchSize") int batchSize);

    @Modifying
    @Query(value = """
            UPDATE outbox_events
            SET
                status = 'PENDING',
                locked_until = NULL,
                next_attempt_at = CURRENT_TIMESTAMP
            WHERE id IN (
                SELECT id
                FROM outbox_events
                WHERE status = 'PUBLISHING'
                  AND locked_until < CURRENT_TIMESTAMP
                ORDER BY locked_until ASC
                LIMIT :batchSize
            )
            """, nativeQuery = true)
    int recoverExpiredPublishingEvents(@Param("batchSize") int batchSize);
}
