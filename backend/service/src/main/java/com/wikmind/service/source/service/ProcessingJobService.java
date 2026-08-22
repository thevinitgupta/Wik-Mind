package com.wikmind.service.source.service;

import com.wikmind.service.common.service.OutboxService;
import com.wikmind.service.source.entity.ProcessingJob;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.repository.ProcessingJobRepository;
import org.springframework.stereotype.Service;

@Service
public class ProcessingJobService {

    private final ProcessingJobRepository processingJobRepository;
    private final OutboxService outboxService;

    public ProcessingJobService(ProcessingJobRepository processingJobRepository, OutboxService outboxService) {
        this.processingJobRepository = processingJobRepository;
        this.outboxService = outboxService;
    }

    public ProcessingJob queue(SourceVersion sourceVersion) {
        ProcessingJob job = ProcessingJob.queued(sourceVersion);
        ProcessingJob savedJob = processingJobRepository.save(job);
        outboxService.queueProcessingJobOutboxEvent(savedJob);
        return savedJob;
    }
}
