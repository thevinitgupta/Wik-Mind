package com.wikmind.service.common.service;

import com.wikmind.service.common.entity.OutboxEvent;
import com.wikmind.service.common.entity.enums.OutboxEventType;
import com.wikmind.service.common.entity.enums.OutboxStatus;
import com.wikmind.service.common.repository.OutboxEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OutboxStateService {
    private final OutboxEventRepository outboxEventRepository;

    public OutboxStateService(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public List<OutboxEvent> claimProcessingJobEvents(int batchSize) {
        PageRequest pageRequest = PageRequest.of(0, batchSize, Sort.by(Sort.Order.desc("created_at"), Sort.Order.asc("attempt_count")));
        List<OutboxEvent> events = outboxEventRepository.findPendingProcessingJobEvents(OutboxEventType.PROCESSING_JOB_QUEUED, OutboxStatus.PENDING, batchSize);

        Instant lockedUntil = Instant.now().plusSeconds(120);

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
