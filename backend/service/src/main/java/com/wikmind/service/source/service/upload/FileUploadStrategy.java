package com.wikmind.service.source.service.upload;

import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.enums.SourceType;
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
    private final StoragePathGeneratorService storagePathGenerator;
    private final ObjectStorageService objectStorageService;

    public FileUploadStrategy(FileValidator fileValidator, StoragePathGeneratorService storagePathGenerator, ObjectStorageService objectStorageService) {
        this.fileValidator = fileValidator;
        this.storagePathGenerator = storagePathGenerator;
        this.objectStorageService = objectStorageService;
    }

    @Override
    public SourceType supports() {
        return SourceType.FILE;
    }

    @Override
    public void upload(SourceVersion sourceVersion, MultipartFile file) {
        fileValidator.validate(file);

        // PROCESSING STATUS IS RESERVED FOR INGESTION QUEUE, NOT HERE
//        sourceVersion.markProcessing();

        String storageKey = storagePathGenerator.source(sourceVersion.getSource().getWorkspace().getId(), sourceVersion.getId(), file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {

            StorageUpload upload = new StorageUpload(storageKey, inputStream, file.getSize(), file.getContentType());

            StorageObject storageObject = objectStorageService.upload(upload);

            // ALSO MARKS UPLOADED STATUS
            sourceVersion.completeUpload(storageObject.key(), file.getContentType(), file.getSize());

            // DONT MARK READY UNTIL PROCESSING IS DONE
//            sourceVersion.markReady();

        } catch (IOException ex) {

            sourceVersion.markFailed("Unable to read uploaded file.");

            throw new StorageUploadException("Failed to read uploaded file.", ex);

        } catch (RuntimeException ex) {

            sourceVersion.markFailed(ex.getMessage());

            throw new StorageUploadException("Failed to upload source to object storage.", ex);
        }
    }
}