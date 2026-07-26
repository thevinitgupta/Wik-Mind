package com.wikmind.service.common.exceptions.workspace;

public class WorkspaceAccessDeniedException extends RuntimeException{
    public WorkspaceAccessDeniedException(String message) {
        super(message);
    }
}
