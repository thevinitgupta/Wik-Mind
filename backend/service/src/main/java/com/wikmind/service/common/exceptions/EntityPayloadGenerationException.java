package com.wikmind.service.common.exceptions;

public class EntityPayloadGenerationException extends RuntimeException {
    public EntityPayloadGenerationException(String message) {
        super(message);
    }
    public EntityPayloadGenerationException(String message, Throwable ex) {
        super(message, ex);
    }
}
