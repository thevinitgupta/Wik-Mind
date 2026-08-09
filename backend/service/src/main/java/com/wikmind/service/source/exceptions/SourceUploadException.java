package com.wikmind.service.source.exceptions;

public class SourceUploadException extends RuntimeException {
    public SourceUploadException(String message) {
        super(message);
    }
  public SourceUploadException(String message, Throwable ex) {
    super(message, ex);
  }
}
