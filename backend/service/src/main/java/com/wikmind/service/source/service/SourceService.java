package com.wikmind.service.source.service;

import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.dto.CreateSourceRequest;
import com.wikmind.service.source.entity.dto.CreateSourceVersionRequest;
import com.wikmind.service.source.entity.dto.SourceResponse;
import com.wikmind.service.source.exceptions.SourceUploadException;
import com.wikmind.service.source.repository.SourceRepository;
import com.wikmind.service.source.repository.SourceVersionRepository;
import com.wikmind.service.source.service.registry.SourceUploadStrategyRegistry;
import com.wikmind.service.source.service.upload.SourceUploadStrategy;
import com.wikmind.service.source.utils.SourceMapper;
import com.wikmind.service.source.utils.SourceNameUtil;
import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.repository.WorkspaceRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SourceService {

    private final SourceUploadStrategyRegistry sourceUploadStrategyRegistry;
    private final WorkspaceRepository workspaceRepository;
    private final SourceRepository sourceRepository;
    private final SourceNameUtil sourceNameUtil;
    private final SourceMapper sourceMapper;
    private final SHA256ChecksumService checksumService;
    private final SourceVersionService sourceVersionService;

    public SourceService(SourceUploadStrategyRegistry sourceUploadStrategyRegistry, WorkspaceRepository workspaceRepository, SourceRepository sourceRepository, SourceNameUtil sourceNameUtil, SourceMapper sourceMapper, SHA256ChecksumService checksumService, SourceVersionService sourceVersionService) {
        this.sourceUploadStrategyRegistry = sourceUploadStrategyRegistry;
        this.workspaceRepository = workspaceRepository;
        this.sourceRepository = sourceRepository;
        this.sourceNameUtil = sourceNameUtil;
        this.sourceMapper = sourceMapper;
        this.checksumService = checksumService;
        this.sourceVersionService = sourceVersionService;
    }

    @Transactional
    public SourceResponse upload(CreateSourceRequest createSourceRequest, UUID workspaceId, UUID userId) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID " + workspaceId + " does not exist"));

        validateWorkspaceAccess(workspace, userId);

        String sourceName = sourceNameUtil.fetchNameFromCreationRequest(createSourceRequest);

        if (sourceName == null || sourceName.isBlank()) {
            throw new SourceUploadException("Something went wrong while fetching name for the source");
        }

        String checksum = checksumService.sha256(createSourceRequest.multipartFile());

        Source source = Source.create(workspace, sourceName, createSourceRequest.sourceType());

        Source savedSource = sourceRepository.save(source);

        SourceVersion savedVersion = sourceVersionService.createInitialVersion(savedSource, sourceName, checksum);

        uploadVersion(savedVersion, source, createSourceRequest);
        savedSource.setLatestVersion(savedVersion);

        sourceRepository.save(savedSource);

        return sourceMapper.toResponse(savedSource);
    }

    private void uploadVersion(SourceVersion sourceVersion, Source source, CreateSourceRequest createSourceRequest) {
        SourceUploadStrategy strategy = sourceUploadStrategyRegistry.get(source.getType());
        strategy.upload(sourceVersion, createSourceRequest.multipartFile());
    }

    public Page<SourceResponse> getSourcesForWorkspace(UUID workspaceId, UUID userId, Pageable pageable) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID " + workspaceId + " does not exist"));

        validateWorkspaceAccess(workspace, userId);

        Page<Source> sources = sourceRepository.findByWorkspaceId(workspaceId, pageable);

        return sources.map(sourceMapper::toResponse);
    }

    private void validateWorkspaceAccess(@NonNull Workspace workspace, UUID userId) {

        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceActionDeniedException("Unauthorized user trying to access workspace: " + workspace.getId());
        }
    }
}