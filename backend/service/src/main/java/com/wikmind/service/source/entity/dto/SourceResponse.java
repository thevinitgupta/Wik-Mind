package com.wikmind.service.source.entity.dto;

import com.wikmind.service.source.entity.enums.SourceStatus;
import com.wikmind.service.source.entity.enums.SourceType;

import java.time.Instant;
import java.util.UUID;

public record SourceResponse(
        UUID id,
        UUID workspaceId,
        String name,
        SourceType type,
        SourceStatus status,
        String mimeType,
        Long size,
        Instant createdAt
) {}
