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

    Page<Source> findByWorkspaceId(
            UUID workspaceId,
            Pageable pageable
    );

    long countByWorkspaceId(
            UUID workspaceId
    );

    void deleteByWorkspace(
            Workspace workspace
    );
}