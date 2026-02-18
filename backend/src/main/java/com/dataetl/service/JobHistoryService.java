package com.dataetl.service;

import com.dataetl.model.ErrorLog;
import com.dataetl.model.JobHistory;
import com.dataetl.model.SourceConfig;
import com.dataetl.model.enums.JobStatus;
import com.dataetl.model.enums.TriggerType;
import com.dataetl.repository.ErrorLogRepository;
import com.dataetl.repository.JobHistoryRepository;
import com.dataetl.repository.SourceConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class JobHistoryService {

    private final JobHistoryRepository jobHistoryRepository;
    private final ErrorLogRepository errorLogRepository;
    private final SourceConfigRepository sourceConfigRepository;

    public JobHistoryService(JobHistoryRepository jobHistoryRepository,
                             ErrorLogRepository errorLogRepository,
                             SourceConfigRepository sourceConfigRepository) {
        this.jobHistoryRepository = jobHistoryRepository;
        this.errorLogRepository = errorLogRepository;
        this.sourceConfigRepository = sourceConfigRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobHistory startJob(SourceConfig config, TriggerType triggeredBy) {
        var job = new JobHistory();
        job.setSourceConfig(config);
        job.setJobName("etl-" + config.getName() + "-" + System.currentTimeMillis());
        job.setStatus(JobStatus.RUNNING);
        job.setStartTime(OffsetDateTime.now());
        job.setTriggeredBy(triggeredBy);
        return jobHistoryRepository.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void completeJob(JobHistory job, JobStatus status, int processed, int errors) {
        job.setStatus(status);
        job.setRecordsProcessed(processed);
        job.setErrorCount(errors);
        job.setEndTime(OffsetDateTime.now());
        jobHistoryRepository.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logError(JobHistory job, String rawData, String errorReason) {
        var error = new ErrorLog();
        error.setJob(job);
        error.setRawData(rawData);
        error.setErrorReason(errorReason);
        error.setOccurredAt(OffsetDateTime.now());
        errorLogRepository.save(error);
    }

    // Runs in its own transaction — safe to call from async threads with no ambient transaction
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateLastRunTimestamp(Long sourceConfigId) {
        sourceConfigRepository.updateLastRunTimestamp(sourceConfigId, OffsetDateTime.now());
    }
}
