package com.wikmind.service.common.service;

import com.wikmind.service.common.entity.OutboxEvent;
import com.wikmind.service.common.entity.dto.ProcessingJobQueuedEvent;
import com.wikmind.service.common.entity.enums.OutboxAggregateType;
import com.wikmind.service.common.entity.enums.OutboxEventType;
import com.wikmind.service.common.repository.OutboxEventRepository;
import com.wikmind.service.common.utils.JSONUtils;
import com.wikmind.service.source.entity.ProcessingJob;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;
    private final JSONUtils jsonUtils;

    public OutboxService(OutboxEventRepository outboxEventRepository, JSONUtils jsonUtils) {
        this.outboxEventRepository = outboxEventRepository;
        this.jsonUtils = jsonUtils;
    }

    public OutboxEvent queueProcessingJobOutboxEvent(ProcessingJob savedJob){
        UUID eventId = UUID.randomUUID();

        ProcessingJobQueuedEvent event =
                ProcessingJobQueuedEvent.from(
                        eventId,
                        savedJob
                );

        String payload = jsonUtils.entityToJSON(event);

        OutboxEvent outboxEvent =
                OutboxEvent.pending(
                        eventId,
                        OutboxEventType.PROCESSING_JOB_QUEUED,
                        OutboxAggregateType.PROCESSING_JOB,
                        savedJob.getId(),
                        payload
                );

        return outboxEventRepository.save(outboxEvent);
    }
}
