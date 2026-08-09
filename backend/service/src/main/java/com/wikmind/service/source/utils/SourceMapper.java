package com.wikmind.service.source.utils;

import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.dto.SourceResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SourceMapper {

   public SourceResponse toResponse(@NonNull Source source){
        return new SourceResponse(
                source.getId(),
                source.getWorkspace().getId(),
                source.getName(),
                source.getType(),
                source.getStatus(),
                source.getMimeType(),
                source.getSize(),
                source.getCreatedAt()
        );
    }

}
