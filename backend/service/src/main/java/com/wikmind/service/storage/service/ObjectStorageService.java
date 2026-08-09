package com.wikmind.service.storage.service;

import com.wikmind.service.storage.dto.StorageDownload;
import com.wikmind.service.storage.dto.StorageMetadata;
import com.wikmind.service.storage.dto.StorageObject;
import com.wikmind.service.storage.dto.StorageUpload;

import java.util.List;

public interface ObjectStorageService {
    StorageObject upload(StorageUpload upload);

    StorageDownload download(String key);

    void delete(String key);

    boolean exists(String key);

    StorageMetadata metadata(String key);

    List<StorageObject> list(String prefix);
}
