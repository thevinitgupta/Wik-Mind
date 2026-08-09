package com.wikmind.service.source.service.upload;

import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.enums.SourceType;
import com.wikmind.service.source.exceptions.DuplicateSourceException;
import com.wikmind.service.source.repository.SourceRepository;
import com.wikmind.service.source.service.SHA256ChecksumService;
import com.wikmind.service.source.service.validator.FileValidator;
import com.wikmind.service.storage.dto.StorageObject;
import com.wikmind.service.storage.dto.StorageUpload;
import com.wikmind.service.storage.exception.StorageUploadException;
import com.wikmind.service.storage.service.ObjectStorageService;
import com.wikmind.service.storage.service.StoragePathGeneratorService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileUploadStrategy implements SourceUploadStrategy {
    private final FileValidator fileValidator;

    private final SHA256ChecksumService checksumService;

    private final SourceRepository sourceRepository;

    private final StoragePathGeneratorService storagePathGenerator;

    private final ObjectStorageService objectStorageService;

    public FileUploadStrategy(FileValidator fileValidator, SHA256ChecksumService checksumService, SourceRepository sourceRepository, StoragePathGeneratorService storagePathGenerator, ObjectStorageService objectStorageService) {
        this.fileValidator = fileValidator;
        this.checksumService = checksumService;
        this.sourceRepository = sourceRepository;
        this.storagePathGenerator = storagePathGenerator;
        this.objectStorageService = objectStorageService;
    }


    @Override
    public SourceType supports() {
        return SourceType.FILE;
    }

    @Override
    public void upload(Source source, MultipartFile file) {
        fileValidator.validate(file);

        source.markProcessing();

        String checksum = source.getChecksum();

        if (sourceRepository.existsByWorkspaceIdAndChecksum(
                source.getWorkspace().getId(),
                checksum
        )) {
            throw new DuplicateSourceException(
                    "Source already exists in workspace."
            );
        }

        String storageKey = storagePathGenerator.source(
                source.getWorkspace().getId(),
                source.getId(),
                file.getContentType()
        );

        try (InputStream inputStream = file.getInputStream()) {

            StorageUpload upload = new StorageUpload(
                    storageKey,
                    inputStream,
                    file.getSize(),
                    file.getContentType()
            );

            StorageObject storageObject = objectStorageService.upload(upload);

            source.completeUpload(
                    storageObject.key(),
                    file.getContentType(),
                    file.getSize()
            );

            source.markReady();

        } catch (IOException ex) {

            source.markFailed("Unable to read uploaded file.");

            throw new StorageUploadException(
                    "Failed to read uploaded file.",
                    ex
            );

        } catch (RuntimeException ex) {

            source.markFailed(ex.getMessage());

            throw new StorageUploadException(
                    "Failed to upload source to object storage.",
                    ex
            );
        }
    }
}
