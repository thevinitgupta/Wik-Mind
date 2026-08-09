package com.wikmind.service.source.repository;

import com.wikmind.service.source.entity.Source;
import com.wikmind.service.source.entity.enums.SourceStatus;
import com.wikmind.service.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {

    boolean existsByWorkspaceIdAndChecksum(
            UUID workspaceId,
            String checksum
    );

    Optional<Source> findByWorkspaceIdAndChecksum(
            UUID workspaceId,
            String checksum
    );

    Page<Source> findByWorkspaceId(
            UUID workspaceId,
            Pageable pageable
    );

    Page<Source> findByWorkspaceIdAndStatus(
            UUID workspaceId,
            SourceStatus status,
            Pageable pageable
    );

    long countByWorkspaceId(UUID workspaceId);

    long countByWorkspaceIdAndStatus(
            UUID workspaceId,
            SourceStatus status
    );

    void deleteByWorkspace(Workspace workspace);
}
