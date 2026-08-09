package com.wikmind.service.storage.dto;

import java.io.InputStream;

public record StorageUpload(
        String key,
        InputStream fileStream,
        Long size,
        String contentType
) {}
