package com.wikmind.service.workspace.service;

import com.wikmind.service.common.exceptions.workspace.WorkspaceAccessDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import com.wikmind.service.users.entity.User;
import com.wikmind.service.users.service.UserService;
import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.entity.dto.WorkspaceResponseDTO;
import com.wikmind.service.workspace.enums.WorkspaceStatus;
import com.wikmind.service.workspace.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserService userService) {
        this.workspaceRepository = workspaceRepository;
        this.userService = userService;
    }

    public WorkspaceResponseDTO createWorkspace(String name, String ownerID){
        User user = userService.fetchUserById(UUID.fromString(ownerID));

        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setOwner(user);
        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return WorkspaceResponseDTO.fromWorkspace(savedWorkspace);
    }

    public Page<WorkspaceResponseDTO> fetchWorkspacesForUser(UUID ownerID, Pageable pageable){
        Page<Workspace> userWorkspaces = workspaceRepository.findAllByOwnerId(ownerID, pageable);
        return userWorkspaces.map(WorkspaceResponseDTO::fromWorkspace);
    }

    public WorkspaceResponseDTO fetchWorkspaceByID(UUID ownerID, UUID workspaceId){
        Optional<Workspace> workspaceOptional = workspaceRepository.findById(workspaceId);
        if(workspaceOptional.isEmpty()){
            throw new WorkspaceNotFoundException("Workspace with ID="+workspaceId+" does not exist");
        }
        Workspace workspace = workspaceOptional.get();
        if(workspace.getOwner().getId().compareTo(ownerID)!=0){
            throw new WorkspaceAccessDeniedException("User: "+ownerID+" does not have access to workspace:"+workspaceId);
        }
        return WorkspaceResponseDTO.fromWorkspace(workspace);
    }

    public void toggleStar(UUID workspaceId){
        Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);

        if(workspaceOpt.isEmpty()){
            throw new WorkspaceNotFoundException("Workspace:"+workspaceId+" cannot be starred as it does not exist");
        }
        Workspace workspace = workspaceOpt.get();

        workspace.setStarred(!workspace.isStarred());

        workspaceRepository.save(workspace);
    }

    public void archive(UUID workspaceId){
        Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);

        if(workspaceOpt.isEmpty()){
            throw new WorkspaceNotFoundException("Workspace:"+workspaceId+" cannot be archived as it does not exist");
        }
        Workspace workspace = workspaceOpt.get();

        if(workspace.getStatus().equals(WorkspaceStatus.ARCHIVED)){
            throw new WorkspaceActionDeniedException("Archive action performed on already archived workspace:"+workspaceId);
        }
        workspace.setStatus(WorkspaceStatus.ARCHIVED);
        workspace.setArchivedAt(Instant.now());

        workspaceRepository.save(workspace);
    }

    public void restore(UUID workspaceId){
        Optional<Workspace> workspaceOpt = workspaceRepository.findById(workspaceId);

        if(workspaceOpt.isEmpty()){
            throw new WorkspaceNotFoundException("Workspace:"+workspaceId+" cannot be restored as it does not exist");
        }
        Workspace workspace = workspaceOpt.get();

        if(workspace.getStatus().equals(WorkspaceStatus.ACTIVE)){
            throw new WorkspaceActionDeniedException("Restore action performed on already active workspace:"+workspaceId);
        }
        workspace.setStatus(WorkspaceStatus.ACTIVE);
        workspace.setArchivedAt(null);

        workspaceRepository.save(workspace);
    }
}
