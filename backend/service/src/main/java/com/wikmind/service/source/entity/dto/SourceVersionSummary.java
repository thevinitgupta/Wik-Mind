package com.wikmind.service.source.entity.dto;

import com.wikmind.service.source.entity.enums.SourceStatus;

import java.time.Instant;
import java.util.UUID;

public record SourceVersionSummary(
        UUID id,
        Integer versionNumber,
        String name,
        SourceStatus status,
        String mimeType,
        Long size,
        Instant createdAt
) {
}
