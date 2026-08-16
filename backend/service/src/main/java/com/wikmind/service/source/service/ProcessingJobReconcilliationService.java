package com.wikmind.service.source.service;

import com.wikmind.service.source.entity.ProcessingJob;
import com.wikmind.service.source.entity.SourceVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessingJobReconcilliationService {
    private final ProcessingJobService processingJobService;
    private final SourceVersionService sourceVersionService;

    public ProcessingJobReconcilliationService(ProcessingJobService processingJobService, SourceVersionService sourceVersionService) {
        this.processingJobService = processingJobService;
        this.sourceVersionService = sourceVersionService;
    }

    public void reconcileMissingProcessingJobs(){
        Page<SourceVersion> unprocessedSourceVersions = sourceVersionService.getUnprocessedSourceVersions(PageRequest.of(0,100));

        for(SourceVersion sv : unprocessedSourceVersions){
            ProcessingJob savedJob = processingJobService.queue(sv);
            log.info("Source Version added to processing jobs| {}-> v{} : JobID={}", sv.getName(),sv.getVersionNumber(), savedJob.getId());
        }
    }
}
