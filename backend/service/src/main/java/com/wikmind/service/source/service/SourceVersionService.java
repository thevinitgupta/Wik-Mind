package com.wikmind.service.source.service;

import com.wikmind.service.common.exceptions.workspace.WorkspaceActionDeniedException;
import com.wikmind.service.common.exceptions.workspace.WorkspaceNotFoundException;
import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.SourceVersion;
import com.wikmind.service.source.entity.dto.CreateSourceRequest;
import com.wikmind.service.source.entity.dto.CreateSourceVersionRequest;
import com.wikmind.service.source.entity.dto.SourceVersionResponse;
import com.wikmind.service.source.exceptions.DuplicateSourceException;
import com.wikmind.service.source.exceptions.SourceUploadException;
import com.wikmind.service.source.repository.ProcessingJobRepository;
import com.wikmind.service.source.repository.SourceRepository;
import com.wikmind.service.source.repository.SourceVersionRepository;
import com.wikmind.service.source.service.registry.SourceUploadStrategyRegistry;
import com.wikmind.service.source.service.upload.SourceUploadStrategy;
import com.wikmind.service.source.utils.SourceVersionMapper;
import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class SourceVersionService {

    private final WorkspaceRepository workspaceRepository;
    private final SourceRepository sourceRepository;
    private final SourceVersionRepository sourceVersionRepository;
    private final SourceUploadStrategyRegistry sourceUploadStrategyRegistry;
    private final SHA256ChecksumService checksumService;
    private final SourceVersionMapper sourceVersionMapper;
    private final ProcessingJobRepository processingJobRepository;
    private final ProcessingJobService processingJobService;

    public SourceVersionService(WorkspaceRepository workspaceRepository, SourceRepository sourceRepository, SourceVersionRepository sourceVersionRepository, SourceUploadStrategyRegistry sourceUploadStrategyRegistry, SHA256ChecksumService checksumService, SourceVersionMapper sourceVersionMapper, ProcessingJobRepository processingJobRepository, ProcessingJobService processingJobService) {
        this.workspaceRepository = workspaceRepository;
        this.sourceRepository = sourceRepository;
        this.sourceVersionRepository = sourceVersionRepository;
        this.sourceUploadStrategyRegistry = sourceUploadStrategyRegistry;
        this.checksumService = checksumService;
        this.sourceVersionMapper = sourceVersionMapper;
        this.processingJobRepository = processingJobRepository;
        this.processingJobService = processingJobService;
    }

    @Transactional
    public SourceVersionResponse createVersion(UUID workspaceId, UUID sourceId, CreateSourceVersionRequest request, UUID userId) {
        Workspace workspace = findWorkspace(workspaceId);
        validateWorkspaceAccess(workspace, userId);

        Source source = findSource(sourceId);
        validateSourceBelongsToWorkspace(source, workspaceId);

        validateUpload(request);

        String checksum = checksumService.sha256(request.multipartFile());

        validateNoDuplicateVersion(sourceId, checksum);

        int nextVersion = getNextVersionNumber(sourceId);

        String versionName = resolveVersionName(request, source);

        SourceVersion sourceVersion = SourceVersion.create(source, nextVersion, versionName, checksum);

        uploadVersion(sourceVersion, source, request);

        SourceVersion savedVersion = sourceVersionRepository.save(sourceVersion);

        updateLatestVersion(source, savedVersion);

        processingJobService.queue(savedVersion);

        return sourceVersionMapper.toResponse(savedVersion);
    }

    public SourceVersion createInitialVersion(Source source, String sourceName, String checksum){
        SourceVersion sourceVersion = SourceVersion.create(source, 1, sourceName, checksum);

        SourceUploadStrategy strategy = sourceUploadStrategyRegistry.get(source.getType());

        return sourceVersionRepository.save(sourceVersion);

    }


    private void updateLatestVersion(Source source, SourceVersion savedVersion) {
        source.setLatestVersion(savedVersion);
    }

    private Workspace findWorkspace(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace with ID " + workspaceId + " does not exist"));
    }

    private Source findSource(UUID sourceId) {
        return sourceRepository.findById(sourceId).orElseThrow(() -> new SourceUploadException("Source with ID " + sourceId + " does not exist"));
    }

    private void validateSourceBelongsToWorkspace(Source source, UUID workspaceId) {
        if (!source.getWorkspace().getId().equals(workspaceId)) {
            throw new WorkspaceActionDeniedException("Source does not belong to workspace: " + workspaceId);
        }
    }

    private void validateUpload(CreateSourceVersionRequest request) {
        if (request.multipartFile() == null || request.multipartFile().isEmpty()) {

            throw new SourceUploadException("A file is required to create a new source version.");
        }
    }

    private void validateNoDuplicateVersion(UUID sourceId, String checksum) {
        if (sourceVersionRepository.existsBySourceIdAndChecksum(sourceId, checksum)) {

            throw new DuplicateSourceException("This file already exists as a version of the source.");
        }
    }

    private int getNextVersionNumber(UUID sourceId) {
        return sourceVersionRepository.findMaxVersionNumber(sourceId).orElse(0) + 1;
    }

    private String resolveVersionName(CreateSourceVersionRequest request, Source source) {
        String name = request.displayName();

        if (name != null && !name.isBlank()) {
            return name;
        }

        String originalFilename = request.multipartFile().getOriginalFilename();

        if (originalFilename != null && !originalFilename.isBlank()) {
            return originalFilename;
        }

        return source.getName();
    }

    private void uploadVersion(SourceVersion sourceVersion, Source source, CreateSourceVersionRequest createSourceVersionRequest) {
        SourceUploadStrategy strategy = sourceUploadStrategyRegistry.get(source.getType());

        strategy.upload(sourceVersion, createSourceVersionRequest.multipartFile());
    }

    public Page<SourceVersion> getUnprocessedSourceVersions(Pageable pageable) {
        return sourceVersionRepository.findAllSourceVersionsWithNoProcessingJobs(pageable);
    }


    private void validateWorkspaceAccess(Workspace workspace, UUID userId) {

        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceActionDeniedException("Unauthorized user trying to access workspace: " + workspace.getId());
        }
    }
}