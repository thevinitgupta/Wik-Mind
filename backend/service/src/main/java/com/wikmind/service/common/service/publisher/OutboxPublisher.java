package com.wikmind.service.common.service.publisher;

import com.wikmind.service.common.entity.OutboxEvent;
import com.wikmind.service.common.entity.dto.ProcessingJobQueuedEvent;
import com.wikmind.service.common.service.OutboxStateService;
import com.wikmind.service.common.service.ProcessingJobEventProducer;
import com.wikmind.service.common.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OutboxPublisher {
    private final static int PUBLISHING_BATCH_SIZE = 50;
    private final static int RECOVERY_BATCH_SIZE = 100;

    private final OutboxStateService outboxStateService;
    private final ProcessingJobEventProducer processingJobEventProducer;
    private final JSONUtils jsonUtils;

    public OutboxPublisher(OutboxStateService outboxStateService, ProcessingJobEventProducer processingJobEventProducer, JSONUtils jsonUtils) {
        this.outboxStateService = outboxStateService;
        this.processingJobEventProducer = processingJobEventProducer;
        this.jsonUtils = jsonUtils;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void publishPendingOutboxProcessingJobEvents() {
        List<OutboxEvent> events = outboxStateService.claimProcessingJobEvents(PUBLISHING_BATCH_SIZE);
        for (OutboxEvent outboxEvent : events) {
            ProcessingJobQueuedEvent payload = jsonUtils.jsonToEntity(outboxEvent.getPayload(), ProcessingJobQueuedEvent.class);

            processingJobEventProducer.publish(payload).whenComplete(((sendResult, exception) -> {
                if (exception == null) {
                    outboxStateService.markPublished(outboxEvent.getId());
                } else {
                    outboxStateService.markPublishFailed(outboxEvent.getId(), exception);
                }
            }));
        }
    }

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.MINUTES)
    public void recoverExpiredPublishingEvents() {
        int recovered =
                outboxStateService
                        .recoverExpiredPublishingEvents(RECOVERY_BATCH_SIZE);

        if (recovered > 0) {
            log.info(
                    "Recovered {} expired outbox events",
                    recovered
            );
        }
    }
}
