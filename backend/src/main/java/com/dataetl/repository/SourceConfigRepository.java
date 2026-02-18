package com.dataetl.repository;

import com.dataetl.model.SourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SourceConfigRepository extends JpaRepository<SourceConfig, Long> {

    List<SourceConfig> findByIsActiveTrueOrderByCreatedAtDesc();

    Optional<SourceConfig> findByName(String name);

    @Modifying
    @Query("UPDATE SourceConfig s SET s.lastRunTimestamp = :timestamp WHERE s.id = :id")
    void updateLastRunTimestamp(@Param("id") Long id, @Param("timestamp") OffsetDateTime timestamp);
}
