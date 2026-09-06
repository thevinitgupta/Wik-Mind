package com.wikmind.service.common.service;

import com.wikmind.service.common.constants.KafkaTopics;
import com.wikmind.service.common.entity.dto.ProcessingJobQueuedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProcessingJobEventProducer {

    private final KafkaTemplate<String, ProcessingJobQueuedEvent> kafkaTemplate;

    public ProcessingJobEventProducer(KafkaTemplate<String, ProcessingJobQueuedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, ProcessingJobQueuedEvent>> publish(ProcessingJobQueuedEvent event){
        return kafkaTemplate.send(
                KafkaTopics.PROCESSING_JOBS,
                event.processingJobId().toString(),
                event
        );
    }
}
