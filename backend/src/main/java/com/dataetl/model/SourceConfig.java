package com.dataetl.model;

import com.dataetl.model.enums.SourceType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "source_config")
public class SourceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType type;

    @Column(name = "connection_string", nullable = false)
    private String connectionString;

    @Type(JsonType.class)
    @Column(name = "auth_config", columnDefinition = "jsonb")
    private Map<String, Object> authConfig;

    @Column(name = "schedule_cron")
    private String scheduleCron;

    @Column(name = "last_run_timestamp")
    private OffsetDateTime lastRunTimestamp;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SourceType getType() { return type; }
    public void setType(SourceType type) { this.type = type; }

    public String getConnectionString() { return connectionString; }
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }

    public Map<String, Object> getAuthConfig() { return authConfig; }
    public void setAuthConfig(Map<String, Object> authConfig) { this.authConfig = authConfig; }

    public String getScheduleCron() { return scheduleCron; }
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }

    public OffsetDateTime getLastRunTimestamp() { return lastRunTimestamp; }
    public void setLastRunTimestamp(OffsetDateTime lastRunTimestamp) { this.lastRunTimestamp = lastRunTimestamp; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
