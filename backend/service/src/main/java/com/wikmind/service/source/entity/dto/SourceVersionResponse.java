package com.wikmind.service.source.entity.dto;

import com.wikmind.service.source.entity.enums.SourceStatus;
import com.wikmind.service.source.entity.enums.SourceType;

import java.time.Instant;
import java.util.UUID;

public record SourceVersionResponse(
        UUID id,
        UUID sourceId,
        UUID workspaceId,
        Integer versionNumber,
        String name,
        SourceType type,
        SourceStatus status,
        String mimeType,
        Long size,
        Instant createdAt
) {
}
