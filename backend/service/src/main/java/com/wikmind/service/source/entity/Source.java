package com.wikmind.service.source.entity;

import com.wikmind.service.source.entity.enums.SourceStatus;
import com.wikmind.service.source.entity.enums.SourceType;
import com.wikmind.service.workspace.entity.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.MimeType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "sources",
        indexes = {
                @Index(
                        name = "idx_source_workspace",
                        columnList = "workspace_id"
                ),
                @Index(
                        name = "idx_source_type",
                        columnList = "type"
                ),
                @Index(
                        name = "idx_source_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Source {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    /**
     * Logical/display name of the source.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Logical source type/integration.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType type;

    /**
     * The currently active/latest version of this source.
     *
     * Nullable while the initial version is being created.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_version_id")
    private SourceVersion latestVersion; // understand how this solves N+1 problem

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Factory method for creating a new logical source.
     */
    public static Source create(
            Workspace workspace,
            String name,
            SourceType type
    ) {
        Source source = new Source();

        source.id = UUID.randomUUID();
        source.workspace = workspace;
        source.name = name;
        source.type = type;

        return source;
    }

    public void setLatestVersion(SourceVersion version) {
        if (!version.getSource().getId().equals(this.id)) {
            throw new IllegalArgumentException(
                    "Version does not belong to this source"
            );
        }

        this.latestVersion = version;
    }

}