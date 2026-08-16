package com.wikmind.service.source.service;

import com.wikmind.service.source.entity.ProcessingJob;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.repository.ProcessingJobRepository;
import org.springframework.stereotype.Service;

@Service
public class ProcessingJobService {

    private final ProcessingJobRepository processingJobRepository;

    public ProcessingJobService(ProcessingJobRepository processingJobRepository) {
        this.processingJobRepository = processingJobRepository;
    }

    public ProcessingJob queue(SourceVersion sourceVersion) {
        ProcessingJob job = ProcessingJob.queued(sourceVersion);
        return processingJobRepository.save(job);
    }
}
