package com.wikmind.service.workspace.repository;

import com.wikmind.service.workspace.entity.Workspace;
import com.wikmind.service.workspace.enums.WorkspaceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Page<Workspace> findAllByOwnerId(UUID ownerID, Pageable pageable);
    List<Workspace> findByStatusAndArchivedAtBefore(
            WorkspaceStatus status,
            Instant archivedBefore
    );
}
