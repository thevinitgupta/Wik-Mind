package com.wikmind.service.source.repository;

import com.wikmind.service.source.entity.ProcessingJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, UUID> {
}
