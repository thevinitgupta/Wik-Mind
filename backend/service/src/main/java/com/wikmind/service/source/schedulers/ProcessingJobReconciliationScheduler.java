package com.wikmind.service.source.schedulers;

import com.wikmind.service.source.entity.ProcessingJob;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.service.ProcessingJobService;
import com.wikmind.service.source.service.SourceVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessingJobReconciliationScheduler {

    private final ProcessingJobService processingJobService;
    private final SourceVersionService sourceVersionService;

    public ProcessingJobReconciliationScheduler(ProcessingJobService processingJobService, SourceVersionService sourceVersionService) {
        this.processingJobService = processingJobService;
        this.sourceVersionService = sourceVersionService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "GMT+5:30")
    public void addSourceVersionToProcessingJobs(){
        Page<SourceVersion> unprocessedSourceVersions = sourceVersionService.getUnprocessedSourceVersions(PageRequest.of(0,100));

        for(SourceVersion sv : unprocessedSourceVersions){
            ProcessingJob savedJob = processingJobService.queue(sv);
            log.info("Source Version added to processing jobs| {}-> v{} : JobID={}", sv.getName(),sv.getVersionNumber(), savedJob.getId());
        }
    }
}
