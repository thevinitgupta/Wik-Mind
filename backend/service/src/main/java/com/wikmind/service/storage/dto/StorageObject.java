package com.wikmind.service.storage.dto;

import java.time.Instant;

public record StorageObject(
        String key,
        long contentLength,
        String eTag
) {
}
