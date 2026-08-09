package com.wikmind.service.source.service;

import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.dto.CreateSourceRequest;
import com.wikmind.service.source.entity.dto.SourceResponse;
import com.wikmind.service.source.entity.enums.SourceType;
import com.wikmind.service.source.exceptions.SourceUploadException;
import com.wikmind.service.source.repository.SourceRepository;
import com.wikmind.service.source.service.registry.SourceUploadStrategyRegistry;
import com.wikmind.service.source.service.upload.SourceUploadStrategy;
import com.wikmind.service.source.utils.SourceMapper;
import com.wikmind.service.source.utils.SourceNameUtil;
import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SourceService {
    private final SourceUploadStrategyRegistry sourceUploadStrategyRegistry;
    private final WorkspaceRepository workspaceRepository;
    private final SourceRepository sourceRepository;
    private final SourceNameUtil sourceNameUtil;
    private final SourceMapper sourceMapper;
    private final SHA256ChecksumService checksumService;

    public SourceService(SourceUploadStrategyRegistry sourceUploadStrategyRegistry, WorkspaceRepository workspaceRepository, SourceRepository sourceRepository, SourceNameUtil sourceNameUtil, SourceMapper sourceMapper, SHA256ChecksumService checksumService) {
        this.sourceUploadStrategyRegistry = sourceUploadStrategyRegistry;
        this.workspaceRepository = workspaceRepository;
        this.sourceRepository = sourceRepository;
        this.sourceNameUtil = sourceNameUtil;
        this.sourceMapper = sourceMapper;
        this.checksumService = checksumService;
        System.out.println("SOURCE UPLOAD STRATEGY FOR FILE:"+sourceUploadStrategyRegistry.get(SourceType.FILE));
    }

    public SourceResponse upload(CreateSourceRequest createSourceRequest, UUID workspaceId, UUID userId){

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID "+workspaceId+" does not exist"));

        if(workspace.getOwner().getId().compareTo(userId)!=0){
            throw new WorkspaceActionDeniedException("Unauthorized user trying to upload sources to workspace:"+workspaceId);
        }

        String sourceName = sourceNameUtil.fetchNameFromCreationRequest(createSourceRequest);
        if(sourceName.isEmpty()) {
            throw new SourceUploadException("Something went wrong while fetching name for the source");
        }
        String checksum = checksumService.sha256(createSourceRequest.multipartFile());
        Source bareSource = Source.create(workspace,sourceName,createSourceRequest.sourceType(), checksum);

        SourceUploadStrategy sourceUploadStrategy = sourceUploadStrategyRegistry.get(createSourceRequest.sourceType());
        sourceUploadStrategy.upload(bareSource,createSourceRequest.multipartFile());
        Source savedSource = sourceRepository.save(bareSource);

        return sourceMapper.toResponse(savedSource);
    }

    public Page<SourceResponse> getSourcesForWorkspace(UUID workspaceId, UUID userId, Pageable pageable){
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID "+workspaceId+" does not exist"));

        if(workspace.getOwner().getId().compareTo(userId)!=0){
            throw new WorkspaceActionDeniedException("Unauthorized user trying to fetch sources from workspace:"+workspaceId);
        }

        Page<Source> sources = sourceRepository.findByWorkspaceId(workspaceId, pageable);

        return sources.map(sourceMapper::toResponse);

    }

}
