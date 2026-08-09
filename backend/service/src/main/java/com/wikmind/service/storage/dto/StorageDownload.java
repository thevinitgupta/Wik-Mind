package com.wikmind.service.storage.dto;

import java.io.InputStream;

public record StorageDownload(
        InputStream content,
        StorageMetadata metadata
) {
}
