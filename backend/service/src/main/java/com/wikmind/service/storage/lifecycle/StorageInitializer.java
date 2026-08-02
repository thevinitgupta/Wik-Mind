package com.wikmind.service.storage.lifecycle;

import com.wikmind.service.storage.config.StorageProperties;
import com.wikmind.service.storage.exception.StorageInitializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
@Slf4j
public class StorageInitializer {
    private final S3Client s3Client;
    private final StorageProperties properties;

    @EventListener(ApplicationReadyEvent.class)
    public void initialize(){
        log.info("==================================================");
        log.info("Initializing Object Storage");
        log.info("Endpoint : {}", properties.getEndpoint());
        log.info("Bucket   : {}", properties.getBucket());

        verifyConnection();
        ensureBucketExists();

        log.info("Object storage initialized successfully.");
        log.info("==================================================");
    }

    private void verifyConnection() {

        log.info("Verifying object storage connection...");

        try {

            s3Client.listBuckets();

            log.info("Successfully connected to object storage.");

        } catch (SdkException ex) {
            throw fail(
                    "Unable to connect to object storage.",
                    ex
            );
        }
    }
    private void ensureBucketExists() {

        log.info("Checking bucket '{}'...", properties.getBucket());

        try {

            s3Client.headBucket(
                    HeadBucketRequest.builder()
                            .bucket(properties.getBucket())
                            .build()
            );

            log.info("Bucket '{}' exists.", properties.getBucket());

        } catch (NoSuchBucketException ex) {

            createBucket();

        } catch (S3Exception ex) {

            if (ex.statusCode() == 404) {
                createBucket();
                return;
            }

            throw fail(
                    "Unable to verify bucket.",
                    ex
            );
        }
    }

    private void createBucket() {

        log.info("Creating bucket '{}'...", properties.getBucket());

        try {

            s3Client.createBucket(
                    CreateBucketRequest.builder()
                            .bucket(properties.getBucket())
                            .build()
            );

            log.info("Bucket created successfully.");

        } catch (BucketAlreadyOwnedByYouException
                 | BucketAlreadyExistsException ignored) {

            log.info("Bucket already exists.");

        } catch (SdkException ex) {

            throw fail(
                    "Unable to create bucket.",
                    ex
            );
        }
    }
    private StorageInitializationException fail(
            String message,
            Exception ex
    ) {

        log.error(
                "{} Bucket='{}'",
                message,
                properties.getBucket(),
                ex
        );

        return new StorageInitializationException(
                message,
                ex
        );
    }
}
