package com.wikmind.service.internal.controllers;

import com.wikmind.service.common.entity.dto.ProcessingJobQueuedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/kafka")
public class KafkaTestController {

    private final KafkaTemplate<String, ProcessingJobQueuedEvent> kafkaTemplate;

    public KafkaTestController(KafkaTemplate<String, ProcessingJobQueuedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {

//        kafkaTemplate.send("wikmind.processing.jobs", "test-key", "hello-from-wikmind");

        return ResponseEntity.ok("Kafka message sent");
    }
}
