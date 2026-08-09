package com.wikmind.service.source.service.upload;

import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.enums.SourceType;
import org.springframework.web.multipart.MultipartFile;

public interface SourceUploadStrategy {
    SourceType supports();

    void upload(Source source, MultipartFile file);
}
