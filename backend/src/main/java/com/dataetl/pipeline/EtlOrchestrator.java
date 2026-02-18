package com.dataetl.pipeline;

import com.dataetl.exception.ResourceNotFoundException;
import com.dataetl.model.JobHistory;
import com.dataetl.model.enums.JobStatus;
import com.dataetl.model.enums.SourceType;
import com.dataetl.model.enums.TriggerType;
import com.dataetl.pipeline.extract.ApiExtractor;
import com.dataetl.pipeline.extract.CsvExtractor;
import com.dataetl.pipeline.extract.DbExtractor;
import com.dataetl.pipeline.extract.Extractor;
import com.dataetl.pipeline.load.UpsertWriter;
import com.dataetl.pipeline.transform.TransformPipeline;
import com.dataetl.repository.JobHistoryRepository;
import com.dataetl.repository.SourceConfigRepository;
import com.dataetl.service.JobHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

@Service
public class EtlOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(EtlOrchestrator.class);

    private final Map<SourceType, Extractor> extractors;
    private final TransformPipeline transformer;
    private final UpsertWriter writer;
    private final JobHistoryService jobHistoryService;
    private final JobHistoryRepository jobHistoryRepository;
    private final SourceConfigRepository sourceConfigRepository;

    public EtlOrchestrator(ApiExtractor apiExtractor,
                           CsvExtractor csvExtractor,
                           DbExtractor dbExtractor,
                           TransformPipeline transformer,
                           UpsertWriter writer,
                           JobHistoryService jobHistoryService,
                           JobHistoryRepository jobHistoryRepository,
                           SourceConfigRepository sourceConfigRepository) {
        this.transformer = transformer;
        this.writer = writer;
        this.jobHistoryService = jobHistoryService;
        this.jobHistoryRepository = jobHistoryRepository;
        this.sourceConfigRepository = sourceConfigRepository;

        this.extractors = new EnumMap<>(SourceType.class);
        this.extractors.put(SourceType.API, apiExtractor);
        this.extractors.put(SourceType.CSV, csvExtractor);
        this.extractors.put(SourceType.DB, dbExtractor);
    }

    public void runJob(Long sourceConfigId, TriggerType triggeredBy) {
        var config = sourceConfigRepository.findById(sourceConfigId)
            .orElseThrow(() -> new ResourceNotFoundException("SourceConfig not found: " + sourceConfigId));

        // Guard: skip if a job for this source is already running — prevents duplicate concurrent runs
        boolean alreadyRunning = jobHistoryRepository
            .findTopBySourceConfigIdAndStatusOrderByStartTimeDesc(sourceConfigId, JobStatus.RUNNING)
            .isPresent();
        if (alreadyRunning) {
            log.warn("{\"event\":\"job_skipped\",\"sourceConfigId\":{},\"reason\":\"already_running\"}", sourceConfigId);
            return;
        }

        JobHistory job = jobHistoryService.startJob(config, triggeredBy);

        log.info("{\"event\":\"job_start\",\"jobId\":{},\"source\":\"{}\",\"trigger\":\"{}\"}",
            job.getId(), config.getName(), triggeredBy);

        int processed = 0;
        int errors = 0;
        var jobStatus = JobStatus.SUCCESS;

        try {
            var extractor = extractors.get(config.getType());
            if (extractor == null) {
                throw new IllegalArgumentException("No extractor available for type: " + config.getType());
            }

            var rawRecords = extractor.extract(config);
            log.info("{\"event\":\"extract_done\",\"jobId\":{},\"rawCount\":{}}", job.getId(), rawRecords.size());

            var validRecords = new ArrayList<Map<String, Object>>();

            // Transform each record individually — bad rows are isolated, not thrown
            for (var raw : rawRecords) {
                var result = transformer.transform(raw, config);
                if (result.isValid()) {
                    validRecords.add(result.record());
                    processed++;
                } else {
                    jobHistoryService.logError(job, raw.toString(), result.errorReason());
                    errors++;
                }
            }

            log.info("{\"event\":\"transform_done\",\"jobId\":{},\"valid\":{},\"invalid\":{}}",
                job.getId(), processed, errors);

            // Batch upsert all valid records
            if (!validRecords.isEmpty()) {
                writer.upsertBatch(validRecords, config);
            }

            // Determine final job status
            if (errors > 0 && processed > 0) {
                jobStatus = JobStatus.PARTIAL;
            } else if (processed == 0 && errors > 0) {
                jobStatus = JobStatus.FAILED;
            }

            // Update last run timestamp for incremental loading on next run
            // Delegated to JobHistoryService which owns REQUIRES_NEW transactions —
            // safe to call from async threads that have no ambient Spring transaction
            jobHistoryService.updateLastRunTimestamp(sourceConfigId);

        } catch (Exception ex) {
            jobStatus = JobStatus.FAILED;
            jobHistoryService.logError(job, null, ex.getMessage());
            log.error("{\"event\":\"job_failed\",\"jobId\":{},\"source\":\"{}\",\"error\":\"{}\"}",
                job.getId(), config.getName(), ex.getMessage());
        } finally {
            jobHistoryService.completeJob(job, jobStatus, processed, errors);
            log.info("{\"event\":\"job_complete\",\"jobId\":{},\"status\":\"{}\",\"processed\":{},\"errors\":{}}",
                job.getId(), jobStatus, processed, errors);
        }
    }
}
