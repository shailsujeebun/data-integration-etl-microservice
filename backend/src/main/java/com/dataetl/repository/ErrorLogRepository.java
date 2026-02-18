package com.dataetl.repository;

import com.dataetl.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    List<ErrorLog> findByJobIdOrderByOccurredAtDesc(Long jobId);

    long countByJobId(Long jobId);
}
