package com.dataetl.dto;

import com.dataetl.model.JobHistory;
import com.dataetl.model.enums.JobStatus;
import com.dataetl.model.enums.TriggerType;

import java.time.Duration;
import java.time.OffsetDateTime;

public record JobHistoryResponse(
    Long id,
    Long sourceConfigId,
    String sourceName,
    String jobName,
    JobStatus status,
    int recordsProcessed,
    int errorCount,
    OffsetDateTime startTime,
    OffsetDateTime endTime,
    TriggerType triggeredBy,
    Long durationMs
) {
    public static JobHistoryResponse from(JobHistory jh) {
        Long durationMs = null;
        if (jh.getStartTime() != null && jh.getEndTime() != null) {
            durationMs = Duration.between(jh.getStartTime(), jh.getEndTime()).toMillis();
        }
        String sourceName = jh.getSourceConfig() != null ? jh.getSourceConfig().getName() : null;
        Long sourceConfigId = jh.getSourceConfig() != null ? jh.getSourceConfig().getId() : null;

        return new JobHistoryResponse(
            jh.getId(),
            sourceConfigId,
            sourceName,
            jh.getJobName(),
            jh.getStatus(),
            jh.getRecordsProcessed(),
            jh.getErrorCount(),
            jh.getStartTime(),
            jh.getEndTime(),
            jh.getTriggeredBy(),
            durationMs
        );
    }
}
