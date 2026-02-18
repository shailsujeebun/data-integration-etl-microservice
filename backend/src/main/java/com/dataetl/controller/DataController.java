package com.dataetl.controller;

import com.dataetl.dto.PageResponse;
import com.dataetl.dto.UnifiedDataResponse;
import com.dataetl.model.enums.SourceType;
import com.dataetl.repository.UnifiedDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/data")
public class DataController {

    // V8: Hard cap on page size — prevents ?size=1000000 loading the entire table into memory
    private static final int MAX_PAGE_SIZE = 100;

    private final UnifiedDataRepository unifiedDataRepository;

    public DataController(UnifiedDataRepository unifiedDataRepository) {
        this.unifiedDataRepository = unifiedDataRepository;
    }

    @GetMapping
    public ResponseEntity<PageResponse<UnifiedDataResponse>> getData(
            @RequestParam(required = false) String sourceType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        SourceType type = null;
        if (sourceType != null && !sourceType.isBlank()) {
            type = SourceType.valueOf(sourceType.toUpperCase());
        }

        // V8: Clamp size to [1, MAX_PAGE_SIZE] — reject negative values and prevent OOM
        int clampedSize = Math.max(1, Math.min(size, MAX_PAGE_SIZE));

        var pageable = PageRequest.of(page, clampedSize);
        var results = unifiedDataRepository.findByFilters(type, from, to, pageable)
            .map(UnifiedDataResponse::from);

        return ResponseEntity.ok(PageResponse.of(results));
    }
}
