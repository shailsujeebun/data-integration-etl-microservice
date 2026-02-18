package com.dataetl.controller;

import com.dataetl.dto.JobDetailResponse;
import com.dataetl.dto.JobHistoryResponse;
import com.dataetl.dto.PageResponse;
import com.dataetl.dto.TriggerRequest;
import com.dataetl.exception.ResourceNotFoundException;
import com.dataetl.model.enums.TriggerType;
import com.dataetl.pipeline.EtlOrchestrator;
import com.dataetl.repository.ErrorLogRepository;
import com.dataetl.repository.JobHistoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private static final int MAX_PAGE_SIZE = 100;

    private final EtlOrchestrator orchestrator;
    private final JobHistoryRepository jobHistoryRepository;
    private final ErrorLogRepository errorLogRepository;
    private final Executor jobExecutor;

    public JobController(EtlOrchestrator orchestrator,
                         JobHistoryRepository jobHistoryRepository,
                         ErrorLogRepository errorLogRepository,
                         @Qualifier("jobExecutor") Executor jobExecutor) {
        this.orchestrator = orchestrator;
        this.jobHistoryRepository = jobHistoryRepository;
        this.errorLogRepository = errorLogRepository;
        this.jobExecutor = jobExecutor;
    }

    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> trigger(@Valid @RequestBody TriggerRequest request) {
        // Run on the dedicated etl-scheduler thread pool, not ForkJoinPool.commonPool()
        CompletableFuture.runAsync(
            () -> orchestrator.runJob(request.sourceConfigId(), TriggerType.MANUAL),
            jobExecutor
        );
        return ResponseEntity.accepted().body(Map.of(
            "message", "Job triggered successfully",
            "sourceConfigId", request.sourceConfigId(),
            "status", "RUNNING"
        ));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<PageResponse<JobHistoryResponse>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        int clampedSize = Math.max(1, Math.min(size, MAX_PAGE_SIZE));
        var pageable = PageRequest.of(page, clampedSize, Sort.by("startTime").descending());
        var jobs = jobHistoryRepository.findAllByOrderByStartTimeDesc(pageable)
            .map(JobHistoryResponse::from);
        return ResponseEntity.ok(PageResponse.of(jobs));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<JobDetailResponse> getJobDetail(@PathVariable Long id) {
        var job = jobHistoryRepository.findByIdWithSourceConfig(id)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + id));
        var errors = errorLogRepository.findByJobIdOrderByOccurredAtDesc(id);
        return ResponseEntity.ok(JobDetailResponse.from(job, errors));
    }
}
