package com.wikmind.service.storage.service.impl;

import com.wikmind.service.storage.config.StorageProperties;
import com.wikmind.service.storage.dto.StorageDownload;
import com.wikmind.service.storage.dto.StorageMetadata;
import com.wikmind.service.storage.dto.StorageObject;
import com.wikmind.service.storage.dto.StorageUpload;
import com.wikmind.service.storage.service.ObjectStorageService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

@Service
public class S3ObjectService implements ObjectStorageService {

    private final S3Client s3Client;
    private final StorageProperties storageProperties;

    public S3ObjectService(S3Client s3Client, StorageProperties storageProperties) {
        this.s3Client = s3Client;
        this.storageProperties = storageProperties;
    }

    @Override
    public StorageObject upload(StorageUpload upload) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storageProperties.getBucket())
                .key(upload.key())
                .contentType(upload.contentType())
                .contentLength(upload.size())
                .checksumAlgorithm(ChecksumAlgorithm.SHA256)
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(
                        upload.fileStream(),
                        upload.size()
                )
        );
        return new StorageObject(
                upload.key(),
                upload.size(),
                putObjectResponse.eTag()
        );
    }

    @Override
    public StorageDownload download(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(storageProperties.getBucket())
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(objectRequest);
        GetObjectResponse response = stream.response();
        StorageMetadata storageMetadata = new StorageMetadata(
                key,
                response.contentLength(),
                response.contentType(),
                response.eTag(),
                response.lastModified()
        );
        return new StorageDownload(
                stream,
                storageMetadata
        );
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(storageProperties.getBucket())
                .key(key)
                .build();
        s3Client.deleteObject(objectRequest);
    }

    @Override
    public boolean exists(String key) {
        try {
            HeadObjectRequest objectRequest = HeadObjectRequest.builder()
                    .bucket(storageProperties.getBucket())
                    .key(key)
                    .build();

            s3Client.headObject(objectRequest);
            return true;
        } catch (NoSuchKeyException e) {

            return false;

        } catch (S3Exception e) {

            if (e.statusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public StorageMetadata metadata(String key) {
        HeadObjectResponse response =
                s3Client.headObject(
                        HeadObjectRequest.builder()
                                .bucket(storageProperties.getBucket())
                                .key(key)
                                .build()
                );

        return new StorageMetadata(
                key,
                response.contentLength(),
                response.contentType(),
                response.eTag(),
                response.lastModified()
        );
    }

    @Override
    public List<StorageObject> list(String prefix) {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(storageProperties.getBucket())
                .prefix(prefix)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsRequest);
        return listObjectsV2Response.contents()
                .stream()
                .map(this::toStorageObject)
                .toList();

    }

    private StorageObject toStorageObject(S3Object object) {

        return new StorageObject(
                object.key(),
                object.size(),
                object.eTag()
        );
    }
}
