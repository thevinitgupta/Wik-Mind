package com.wikmind.service.common.entity.dto;

import com.wikmind.service.source.entity.ProcessingJob;

import java.util.UUID;

public record ProcessingJobQueuedEvent(
        UUID eventId,
        UUID processingJobId,
        UUID sourceVersionId,
        UUID sourceId,
        UUID workspaceId,
        int attempt
) {

    public static ProcessingJobQueuedEvent from(UUID eventId, ProcessingJob savedJob){
        return new ProcessingJobQueuedEvent(
                eventId,
                savedJob.getId(),
                savedJob.getSourceVersion().getId(),
                savedJob.getSourceVersion().getSource().getId(),
                savedJob.getSourceVersion().getSource().getWorkspace().getId(),
                savedJob.getAttemptCount()
        );
    }
}
