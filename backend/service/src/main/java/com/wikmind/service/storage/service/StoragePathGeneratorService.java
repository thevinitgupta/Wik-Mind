package com.wikmind.service.storage.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StoragePathGeneratorService {

    public String source(UUID workspaceId, UUID sourceId, String filename) {
        String extension = getExtension(filename);

        if (extension.isBlank()) {
            return String.format("workspaces/%s/sources/%s/original", workspaceId, sourceId);
        }

        return String.format("workspaces/%s/sources/%s/original.%s", workspaceId, sourceId, extension);
    }

    private String getExtension(String filename) {

        int lastDot = filename.lastIndexOf('.');

        if (lastDot == -1 || lastDot == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDot + 1).toLowerCase();
    }
}