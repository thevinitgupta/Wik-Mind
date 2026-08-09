package com.wikmind.service.storage.exception;

public class StorageUploadException extends RuntimeException {
    public StorageUploadException(String message, Throwable ex) {
        super(message,ex);
    }
}
