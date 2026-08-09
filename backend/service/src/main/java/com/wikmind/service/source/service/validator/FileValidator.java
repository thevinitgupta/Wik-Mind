package com.wikmind.service.source.service.validator;

import com.wikmind.service.source.exceptions.InvalidSourceException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class FileValidator {
    private static final Set<String> SUPPORTED_TYPES = Set.of(
            MediaType.APPLICATION_PDF_VALUE,
            MediaType.TEXT_PLAIN_VALUE,
            "text/markdown"
    );

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidSourceException("Uploaded file is empty.");
        }

        if (file.getOriginalFilename() == null ||
                file.getOriginalFilename().isBlank()) {

            throw new InvalidSourceException(
                    "File name is required."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new InvalidSourceException(
                    "Maximum upload size exceeded."
            );
        }

        if (!SUPPORTED_TYPES.contains(file.getContentType())) {

            throw new InvalidSourceException(
                    "Unsupported file type."
            );
        }
    }
}
