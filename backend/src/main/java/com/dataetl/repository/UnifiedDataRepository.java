package com.dataetl.repository;

import com.dataetl.model.UnifiedData;
import com.dataetl.model.enums.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface UnifiedDataRepository extends JpaRepository<UnifiedData, Long> {

    @Query("""
        SELECT u FROM UnifiedData u
        WHERE (:sourceType IS NULL OR u.sourceType = :sourceType)
          AND (:from IS NULL OR u.ingestedAt >= :from)
          AND (:to IS NULL OR u.ingestedAt <= :to)
        ORDER BY u.ingestedAt DESC
        """)
    Page<UnifiedData> findByFilters(
        @Param("sourceType") SourceType sourceType,
        @Param("from") OffsetDateTime from,
        @Param("to") OffsetDateTime to,
        Pageable pageable
    );
}
