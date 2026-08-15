package com.wikmind.service.source.utils;

import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.dto.SourceResponse;
import com.wikmind.service.source.entity.dto.SourceVersionSummary;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SourceMapper {

    public SourceResponse toResponse(@NonNull Source source) {

        SourceVersion latestVersion = source.getLatestVersion();

        SourceVersionSummary latestVersionResponse = getLatestVersionResponse(latestVersion);

        return new SourceResponse(
                source.getId(),
                source.getWorkspace().getId(),
                source.getName(),
                source.getType(),
                latestVersionResponse,
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }

    private static @Nullable SourceVersionSummary getLatestVersionResponse(SourceVersion latestVersion) {
        SourceVersionSummary latestVersionResponse = null;

        if (latestVersion != null) {
            latestVersionResponse = new SourceVersionSummary(
                    latestVersion.getId(),
                    latestVersion.getVersionNumber(),
                    latestVersion.getName(),
                    latestVersion.getStatus(),
                    latestVersion.getMimeType(),
                    latestVersion.getSize(),
                    latestVersion.getCreatedAt()
            );
        }
        return latestVersionResponse;
    }
}
