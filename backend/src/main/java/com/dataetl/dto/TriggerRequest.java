package com.dataetl.dto;

import jakarta.validation.constraints.NotNull;

public record TriggerRequest(
    @NotNull(message = "sourceConfigId is required")
    Long sourceConfigId
) {}
