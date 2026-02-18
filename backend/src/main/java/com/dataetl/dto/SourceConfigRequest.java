package com.dataetl.dto;

import com.dataetl.model.enums.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record SourceConfigRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotNull(message = "Type is required (API, CSV, or DB)")
    SourceType type,

    @NotBlank(message = "Connection string is required")
    String connectionString,

    Map<String, Object> authConfig,

    String scheduleCron,

    Boolean isActive
) {}
