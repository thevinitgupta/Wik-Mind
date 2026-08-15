package com.wikmind.service.source.entity.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateSourceVersionRequest(MultipartFile multipartFile, String displayName) {
}
