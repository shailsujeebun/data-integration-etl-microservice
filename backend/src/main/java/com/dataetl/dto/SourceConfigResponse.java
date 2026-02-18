package com.dataetl.dto;

import com.dataetl.model.SourceConfig;
import com.dataetl.model.enums.SourceType;

import java.time.OffsetDateTime;

public record SourceConfigResponse(
    Long id,
    String name,
    SourceType type,
    String connectionString,
    // authConfig intentionally excluded — it contains bearer tokens and API keys.
    // hasAuthConfig lets the UI show a lock icon without exposing the secret values.
    boolean hasAuthConfig,
    String scheduleCron,
    OffsetDateTime lastRunTimestamp,
    Boolean isActive,
    OffsetDateTime createdAt
) {
    public static SourceConfigResponse from(SourceConfig sc) {
        return new SourceConfigResponse(
            sc.getId(),
            sc.getName(),
            sc.getType(),
            sc.getConnectionString(),
            sc.getAuthConfig() != null && !sc.getAuthConfig().isEmpty(),
            sc.getScheduleCron(),
            sc.getLastRunTimestamp(),
            sc.getIsActive(),
            sc.getCreatedAt()
        );
    }
}
