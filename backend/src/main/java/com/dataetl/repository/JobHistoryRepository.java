package com.dataetl.repository;

import com.dataetl.model.JobHistory;
import com.dataetl.model.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {

    // JOIN FETCH sourceConfig so we can access sourceName without lazy-load errors
    @Query("SELECT j FROM JobHistory j LEFT JOIN FETCH j.sourceConfig ORDER BY j.startTime DESC")
    List<JobHistory> findAllWithSourceConfig();

    // Paginated version — note: JPQL JOIN FETCH + pagination requires count query
    @Query(
        value = "SELECT j FROM JobHistory j LEFT JOIN FETCH j.sourceConfig ORDER BY j.startTime DESC",
        countQuery = "SELECT COUNT(j) FROM JobHistory j"
    )
    Page<JobHistory> findAllByOrderByStartTimeDesc(Pageable pageable);

    @Query("SELECT j FROM JobHistory j LEFT JOIN FETCH j.sourceConfig WHERE j.sourceConfig.id = :sourceConfigId ORDER BY j.startTime DESC")
    List<JobHistory> findBySourceConfigIdOrderByStartTimeDesc(Long sourceConfigId);

    @Query("SELECT j FROM JobHistory j LEFT JOIN FETCH j.sourceConfig WHERE j.id = :id")
    Optional<JobHistory> findByIdWithSourceConfig(Long id);

    Optional<JobHistory> findTopBySourceConfigIdAndStatusOrderByStartTimeDesc(
        Long sourceConfigId, JobStatus status
    );
}
