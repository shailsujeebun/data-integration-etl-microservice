package com.dataetl.controller;

import com.dataetl.dto.SourceConfigRequest;
import com.dataetl.dto.SourceConfigResponse;
import com.dataetl.exception.ResourceNotFoundException;
import com.dataetl.model.SourceConfig;
import com.dataetl.model.enums.SourceType;
import com.dataetl.pipeline.extract.SsrfGuard;
import com.dataetl.repository.SourceConfigRepository;
import com.dataetl.service.DynamicSchedulerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sources")
public class SourceController {

    private final SourceConfigRepository sourceConfigRepository;
    private final DynamicSchedulerService schedulerService;

    public SourceController(SourceConfigRepository sourceConfigRepository,
                            DynamicSchedulerService schedulerService) {
        this.sourceConfigRepository = sourceConfigRepository;
        this.schedulerService = schedulerService;
    }

    @GetMapping
    public ResponseEntity<List<SourceConfigResponse>> getAllSources() {
        var sources = sourceConfigRepository.findAll()
            .stream()
            .map(SourceConfigResponse::from)
            .toList();
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceConfigResponse> getSource(@PathVariable Long id) {
        var source = sourceConfigRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Source not found: " + id));
        return ResponseEntity.ok(SourceConfigResponse.from(source));
    }

    @PostMapping
    public ResponseEntity<SourceConfigResponse> createSource(@Valid @RequestBody SourceConfigRequest request) {
        // V6: validate cron expression before saving — prevents storing broken schedules
        // that only fail at runtime inside DynamicSchedulerService
        validateCron(request.scheduleCron());

        // V3: validate connectionString at save time — prevents SSRF by rejecting private/internal
        // addresses before they ever reach a job runner (guard also fires again at extraction time)
        validateConnectionString(request.type(), request.connectionString());

        var sc = new SourceConfig();
        sc.setName(request.name());
        sc.setType(request.type());
        sc.setConnectionString(request.connectionString());
        sc.setAuthConfig(request.authConfig());
        sc.setScheduleCron(request.scheduleCron());
        sc.setIsActive(request.isActive() != null ? request.isActive() : true);

        var saved = sourceConfigRepository.save(sc);

        // Register with scheduler if cron is provided
        if (saved.getScheduleCron() != null && saved.getIsActive()) {
            schedulerService.scheduleSource(saved);
        }

        return ResponseEntity.status(201).body(SourceConfigResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourceConfigResponse> updateSource(
            @PathVariable Long id,
            @Valid @RequestBody SourceConfigRequest request) {

        // V6: validate cron expression before saving
        validateCron(request.scheduleCron());

        // V3: validate connectionString at save time (same guard fires again at extraction time)
        validateConnectionString(request.type(), request.connectionString());

        var sc = sourceConfigRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Source not found: " + id));

        sc.setName(request.name());
        sc.setType(request.type());
        sc.setConnectionString(request.connectionString());
        sc.setAuthConfig(request.authConfig());
        sc.setScheduleCron(request.scheduleCron());
        if (request.isActive() != null) sc.setIsActive(request.isActive());

        var saved = sourceConfigRepository.save(sc);

        // Re-register schedule (handles cron change or deactivation)
        schedulerService.scheduleSource(saved);

        return ResponseEntity.ok(SourceConfigResponse.from(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        if (!sourceConfigRepository.existsById(id)) {
            throw new ResourceNotFoundException("Source not found: " + id);
        }
        schedulerService.cancelSchedule(id);
        sourceConfigRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Validates a Spring 6-part cron expression (seconds included).
     * CronExpression.isValidExpression() returns false for invalid expressions;
     * we throw IllegalArgumentException which GlobalExceptionHandler maps to 400.
     */
    private void validateCron(String cron) {
        if (cron == null || cron.isBlank()) return; // null/blank = no schedule, valid
        if (!CronExpression.isValidExpression(cron)) {
            throw new IllegalArgumentException(
                "Invalid cron expression: '" + cron +
                "'. Spring cron requires 6 fields (seconds minutes hours day month weekday). " +
                "Example: '0 0/30 * * * ?' = every 30 minutes."
            );
        }
    }

    /**
     * V3: SSRF guard applied at save time so malicious connection strings are rejected
     * before they are ever stored in the database or reach a job runner.
     * - API sources: must be a non-private HTTP/HTTPS URL
     * - CSV sources: must be a file path within /app/sample-data/
     * - DB sources: no URL validation needed (JDBC URLs don't carry the same SSRF surface
     *   and DbExtractor already enforces SELECT-only + parameterized queries)
     */
    private void validateConnectionString(SourceType type, String connectionString) {
        if (type == null || connectionString == null || connectionString.isBlank()) return;
        if (type == SourceType.API) {
            SsrfGuard.assertSafeUrl(connectionString);
        } else if (type == SourceType.CSV) {
            SsrfGuard.assertSafeFilePath(connectionString);
        }
    }
}
