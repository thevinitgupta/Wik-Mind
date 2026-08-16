package com.wikmind.service.source.schedulers;

import com.wikmind.service.source.service.ProcessingJobReconcilliationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProcessingJobReconciliationScheduler {
    private final ProcessingJobReconcilliationService processingJobReconcilliationService;

    public ProcessingJobReconciliationScheduler(ProcessingJobReconcilliationService processingJobReconcilliationService) {
        this.processingJobReconcilliationService = processingJobReconcilliationService;
    }

    @Scheduled(initialDelay = 10, fixedDelay = 15, timeUnit = TimeUnit.MINUTES)
    public void addSourceVersionToProcessingJobs(){
        processingJobReconcilliationService.reconcileMissingProcessingJobs();
    }
}
