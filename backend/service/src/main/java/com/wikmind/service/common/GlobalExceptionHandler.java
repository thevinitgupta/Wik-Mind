package com.wikmind.service.common;

import com.wikmind.service.common.exceptions.workspace.WorkspaceAccessDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundHandler(UsernameNotFoundException ex){
        LOG.error("Username not found: {}", ex.getName(), ex);
        return ResponseEntity.badRequest().body("Invalid user");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<String> authenticationServiceHandler(AuthenticationServiceException ex){
        LOG.error("Authentication service failed: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<String> workspaceNotFoundHandler(WorkspaceNotFoundException ex){
        LOG.error("Workspace fetched not found: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Workspace not found");
    }

    @ExceptionHandler(WorkspaceAccessDeniedException.class)
    public ResponseEntity<String> workspaceAccessHandler(WorkspaceAccessDeniedException ex){
        LOG.error("Current User does not have access to workspace: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have access");
    }

    @ExceptionHandler(WorkspaceActionDeniedException.class)
    public ResponseEntity<String> workspaceACtionDeniedHandler(WorkspaceActionDeniedException ex){
        LOG.error("Action Being performed to workspace is not allowed: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Action cannot be performed for Workspace");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> authenticationServiceHandler(Exception ex){
        LOG.error("Unhandled exception : ", ex);
        return ResponseEntity.internalServerError().body("It's not you, it's us. Please wait and retry");
    }
}
