package com.wikmind.service.workspace.entity.dto;

import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.enums.ClonePolicy;
import com.wikmind.service.workspace.enums.Visibility;
import com.wikmind.service.workspace.enums.WorkspaceStatus;

import java.time.Instant;

public record WorkspaceResponseDTO(
        String id,
        String name,
        Visibility visibility,
        ClonePolicy clonePolicy,
        WorkspaceStatus status,
        boolean starred,
        Instant updatedAt
) {
    public static WorkspaceResponseDTO fromWorkspace(Workspace workspace){
        return new WorkspaceResponseDTO(workspace.getId().toString(),workspace.getName(), workspace.getVisibility(), workspace.getClonePolicy(),workspace.getStatus(), workspace.isStarred(), workspace.getUpdatedAt());
    }
}
