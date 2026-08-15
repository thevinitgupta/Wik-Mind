package com.wikmind.service.source.utils;

import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.dto.SourceVersionResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SourceVersionMapper {

    public SourceVersionResponse toResponse(
            @NonNull SourceVersion sourceVersion
    ) {
        return new SourceVersionResponse(
                sourceVersion.getId(),
                sourceVersion.getSource().getId(),
                sourceVersion.getSource().getWorkspace().getId(),
                sourceVersion.getVersionNumber(),
                sourceVersion.getName(),
                sourceVersion.getSource().getType(),
                sourceVersion.getStatus(),
                sourceVersion.getMimeType(),
                sourceVersion.getSize(),
                sourceVersion.getCreatedAt()
        );
    }
}
