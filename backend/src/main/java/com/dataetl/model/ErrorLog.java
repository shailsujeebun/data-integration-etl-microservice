package com.dataetl.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "error_log")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobHistory job;

    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;

    @Column(name = "error_reason", columnDefinition = "TEXT")
    private String errorReason;

    @Column(name = "occurred_at")
    private OffsetDateTime occurredAt = OffsetDateTime.now();

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobHistory getJob() { return job; }
    public void setJob(JobHistory job) { this.job = job; }

    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }

    public String getErrorReason() { return errorReason; }
    public void setErrorReason(String errorReason) { this.errorReason = errorReason; }

    public OffsetDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(OffsetDateTime occurredAt) { this.occurredAt = occurredAt; }
}
