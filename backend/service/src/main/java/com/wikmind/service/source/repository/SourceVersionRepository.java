package com.wikmind.service.source.repository;

import com.wikmind.service.source.entity.SourceVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SourceVersionRepository extends JpaRepository<SourceVersion, UUID> {
    boolean existsBySourceIdAndChecksum(
            UUID sourceId,
            String checksum
    );

    Optional<SourceVersion> findBySourceIdAndVersionNumber(
            UUID sourceId,
            Integer versionNumber
    );

    @Query("""
        select sv from SourceVersion sv
        LEFT JOIN ProcessingJob pj
        ON sv.id = pj.sourceVersion.id
        WHERE sv.status = 'UPLOADED' AND pj.id IS NULL
        ORDER BY sv.createdAt DESC
    """)
    Page<SourceVersion> findAllSourceVersionsWithNoProcessingJobs(Pageable pageable);

    @Query("""
        SELECT MAX(sv.versionNumber)
        FROM SourceVersion sv
        WHERE sv.source.id = :sourceId
    """)
    Optional<Integer> findMaxVersionNumber(
            @Param("sourceId") UUID sourceId
    );
}
