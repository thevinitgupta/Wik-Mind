package com.wikmind.service.source.repository;

import com.wikmind.service.source.entity.SourceVersion;
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
        SELECT MAX(sv.versionNumber)
        FROM SourceVersion sv
        WHERE sv.source.id = :sourceId
    """)
    Optional<Integer> findMaxVersionNumber(
            @Param("sourceId") UUID sourceId
    );
}
