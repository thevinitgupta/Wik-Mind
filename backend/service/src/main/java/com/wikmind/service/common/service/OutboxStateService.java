package com.wikmind.service.common.service;

import com.wikmind.service.common.entity.OutboxEvent;
import com.wikmind.service.common.entity.enums.OutboxEventType;
import com.wikmind.service.common.entity.enums.OutboxStatus;
import com.wikmind.service.common.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OutboxStateService {
    private static final int LOCKED_LEASE_DURATION_SECONDS = 120;
    private final OutboxEventRepository outboxEventRepository;

    public OutboxStateService(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public List<OutboxEvent> claimProcessingJobEvents(int batchSize) {
        List<OutboxEvent> events = outboxEventRepository.findPendingProcessingJobEvents(OutboxEventType.PROCESSING_JOB_QUEUED, OutboxStatus.PENDING, batchSize);

        Instant lockedUntil = Instant.now().plusSeconds(LOCKED_LEASE_DURATION_SECONDS);

        events.forEach(outboxEvent -> {
            outboxEvent.markPublishing(lockedUntil);
        });
        return events;
    }

    @Transactional
    public void markPublished(UUID eventId) {

        OutboxEvent event = outboxEventRepository.findById(eventId).orElseThrow();
        event.markPublished(Instant.now());
    }

    @Transactional
    public void markPublishFailed(UUID eventId, Throwable exception) {

        OutboxEvent event = outboxEventRepository.findById(eventId).orElseThrow();

        event.markPublishFailed(exception.getMessage(), calculateNextAttemptAt(event));
    }

    @Transactional
    public int recoverExpiredPublishingEvents(int batchSize) {
        return outboxEventRepository.recoverExpiredPublishingEvents(batchSize);
    }

    private Instant calculateNextAttemptAt(OutboxEvent event) {
        // TODO: Make this exponential backoff strategy with failure catching
        return Instant.now().plusSeconds(1000);
    }
}
