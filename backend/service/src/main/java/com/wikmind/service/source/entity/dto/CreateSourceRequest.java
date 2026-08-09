package com.wikmind.service.source.entity.dto;

import com.wikmind.service.source.entity.enums.SourceType;
import org.springframework.web.multipart.MultipartFile;

public record CreateSourceRequest(
        SourceType sourceType,
        MultipartFile multipartFile,
        String url,
        String displayName
) {}
