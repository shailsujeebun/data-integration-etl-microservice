package com.dataetl.model;

import com.dataetl.model.enums.JobStatus;
import com.dataetl.model.enums.TriggerType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job_history")
public class JobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_config_id")
    private SourceConfig sourceConfig;

    @Column(name = "job_name")
    private String jobName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(name = "records_processed")
    private int recordsProcessed = 0;

    @Column(name = "error_count")
    private int errorCount = 0;

    @Column(name = "start_time")
    private OffsetDateTime startTime = OffsetDateTime.now();

    @Column(name = "end_time")
    private OffsetDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "triggered_by", nullable = false)
    private TriggerType triggeredBy;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SourceConfig getSourceConfig() { return sourceConfig; }
    public void setSourceConfig(SourceConfig sourceConfig) { this.sourceConfig = sourceConfig; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public int getRecordsProcessed() { return recordsProcessed; }
    public void setRecordsProcessed(int recordsProcessed) { this.recordsProcessed = recordsProcessed; }

    public int getErrorCount() { return errorCount; }
    public void setErrorCount(int errorCount) { this.errorCount = errorCount; }

    public OffsetDateTime getStartTime() { return startTime; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }

    public OffsetDateTime getEndTime() { return endTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }

    public TriggerType getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(TriggerType triggeredBy) { this.triggeredBy = triggeredBy; }
}
