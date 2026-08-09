package com.wikmind.service.workspace.controller;

import com.wikmind.service.auth.entity.AuthenticatedUser;
import com.wikmind.service.source.entity.dto.CreateSourceRequest;
import com.wikmind.service.source.entity.dto.SourceResponse;
import com.wikmind.service.source.service.SourceService;
import com.wikmind.service.workspace.entity.dto.WorkspaceCreationRequestDTO;
import com.wikmind.service.workspace.entity.dto.WorkspaceResponseDTO;
import com.wikmind.service.workspace.service.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/workspaces")
public class WorkspaceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceController.class);
    private final WorkspaceService workspaceService;
    private final SourceService sourceService;

    public WorkspaceController(WorkspaceService workspaceService, SourceService sourceService) {
        this.workspaceService = workspaceService;
        this.sourceService = sourceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponseDTO> createWorkspace(@RequestBody WorkspaceCreationRequestDTO workspaceCreationDTO, @AuthenticationPrincipal AuthenticatedUser authenticatedUser){
        WorkspaceResponseDTO workspaceResponseDTO = workspaceService.createWorkspace(workspaceCreationDTO.name(), authenticatedUser.getUserId().toString());

        return ResponseEntity.ok(workspaceResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<WorkspaceResponseDTO>> getCurrentUserWorkspace(
            @PageableDefault(page = 0, size = 5, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser){
            return ResponseEntity.ok(workspaceService.fetchWorkspacesForUser(authenticatedUser.getUserId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> getWorkspaceDetails(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID id){
        return ResponseEntity.ok(workspaceService.fetchWorkspaceByID(authenticatedUser.getUserId(), id));
    }

    @PatchMapping("/{workspaceId}/star")
    public ResponseEntity<Void> toggleStar(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID workspaceId
    ) {
        workspaceService.toggleStar(workspaceId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{workspaceId}/archive")
    public ResponseEntity<Void> archiveWorkspace(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID workspaceId
    ) {
        workspaceService.archive(workspaceId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{workspaceId}/restore")
    public ResponseEntity<Void> restoreWorkspace(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID workspaceId
    ) {
        workspaceService.restore(workspaceId);

        return ResponseEntity.noContent().build();
    }

    /** Sources **/

    @PostMapping(value = "/{workspaceId}/sources",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<SourceResponse> uploadSource( @ModelAttribute CreateSourceRequest createSourceRequest,
                                                       @PathVariable UUID workspaceId,
                                                       @AuthenticationPrincipal AuthenticatedUser authenticatedUser){

        SourceResponse sourceResponse = sourceService.upload(createSourceRequest,workspaceId,authenticatedUser.getUserId());
        return ResponseEntity.ok().body(sourceResponse);
    }

    @GetMapping(value = "/{workspaceId}/sources"
    )
    public ResponseEntity<Page<SourceResponse>> fetchSourcesForWorkspace(
                                                             @PathVariable UUID workspaceId,
                                                             @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser){

        Page<SourceResponse> sources = sourceService.getSourcesForWorkspace(workspaceId,authenticatedUser.getUserId(), pageable);
        return ResponseEntity.ok().body(sources);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleWorkspaceException(Exception ex){
        LOGGER.error("Error in workspace controller ", ex);
        return ResponseEntity.internalServerError().body("Something went wrong, please try again");
    }
}

