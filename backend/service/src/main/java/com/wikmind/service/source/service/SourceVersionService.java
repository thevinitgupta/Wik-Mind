package com.wikmind.service.source.service;

import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.dto.CreateSourceVersionRequest;
import com.wikmind.service.source.entity.dto.SourceVersionResponse;
import com.wikmind.service.source.exceptions.DuplicateSourceException;
import com.wikmind.service.source.exceptions.SourceUploadException;
import com.wikmind.service.source.repository.SourceRepository;
import com.wikmind.service.source.repository.SourceVersionRepository;
import com.wikmind.service.source.service.registry.SourceUploadStrategyRegistry;
import com.wikmind.service.source.service.upload.SourceUploadStrategy;
import com.wikmind.service.source.utils.SourceVersionMapper;
import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SourceVersionService {

    private final WorkspaceRepository workspaceRepository;
    private final SourceRepository sourceRepository;
    private final SourceVersionRepository sourceVersionRepository;
    private final SourceUploadStrategyRegistry sourceUploadStrategyRegistry;
    private final SHA256ChecksumService checksumService;
    private final SourceVersionMapper sourceVersionMapper;

    public SourceVersionService(WorkspaceRepository workspaceRepository, SourceRepository sourceRepository, SourceVersionRepository sourceVersionRepository, SourceUploadStrategyRegistry sourceUploadStrategyRegistry, SHA256ChecksumService checksumService, SourceVersionMapper sourceVersionMapper) {
        this.workspaceRepository = workspaceRepository;
        this.sourceRepository = sourceRepository;
        this.sourceVersionRepository = sourceVersionRepository;
        this.sourceUploadStrategyRegistry = sourceUploadStrategyRegistry;
        this.checksumService = checksumService;
        this.sourceVersionMapper = sourceVersionMapper;
    }

    @Transactional
    public SourceVersionResponse createVersion(UUID workspaceId, UUID sourceId, CreateSourceVersionRequest request, UUID userId) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID " + workspaceId + " does not exist"));

        validateWorkspaceAccess(workspace, workspaceId, userId);

        Source source = sourceRepository.findById(sourceId).orElseThrow(() -> new SourceUploadException("Source with ID " + sourceId + " does not exist"));

        if (!source.getWorkspace().getId().equals(workspaceId)) {
            throw new WorkspaceActionDeniedException("Source does not belong to workspace: " + workspaceId);
        }


        // TODO: Should be updated to support different source types
        if (request.multipartFile() == null || request.multipartFile().isEmpty()) {
            throw new SourceUploadException("A file is required to create a new source version.");
        }

        String checksum = checksumService.sha256(request.multipartFile());

        if (sourceVersionRepository.existsBySourceIdAndChecksum(sourceId, checksum)) {
            throw new DuplicateSourceException("This file already exists as a version of the source.");
        }

        int nextVersion = sourceVersionRepository.findMaxVersionNumber(sourceId).orElse(0) + 1;

        String versionName = request.displayName();

        if (versionName == null || versionName.isBlank()) {
            versionName = request.multipartFile().getOriginalFilename();
        }

        if (versionName == null || versionName.isBlank()) {
            versionName = source.getName();
        }

        SourceVersion sourceVersion = SourceVersion.create(source, nextVersion, versionName, checksum);

        SourceUploadStrategy strategy = sourceUploadStrategyRegistry.get(source.getType());
        strategy.upload(sourceVersion, request.multipartFile());

        SourceVersion savedVersion = sourceVersionRepository.save(sourceVersion);

        source.setLatestVersion(savedVersion);

        sourceRepository.save(source);

        return sourceVersionMapper.toResponse(savedVersion);
    }

    private void validateWorkspaceAccess(Workspace workspace, UUID workspaceId, UUID userId) {

        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceActionDeniedException("Unauthorized user trying to access workspace: " + workspaceId);
        }
    }
}