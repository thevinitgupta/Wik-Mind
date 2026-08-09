package com.wikmind.service.storage.dto;

import java.time.Instant;

public record StorageMetadata(
        String key,
        long contentLength,
        String contentType,
        String eTag,
        Instant lastModified
) {}