package com.dataetl.dto;

import com.dataetl.model.UnifiedData;
import com.dataetl.model.enums.SourceType;

import java.time.OffsetDateTime;
import java.util.Map;

public record UnifiedDataResponse(
    Long id,
    Long sourceConfigId,
    String externalId,
    SourceType sourceType,
    String sourceName,
    Map<String, Object> payload,
    OffsetDateTime ingestedAt,
    OffsetDateTime updatedAt
) {
    public static UnifiedDataResponse from(UnifiedData ud) {
        Long sourceConfigId = ud.getSourceConfig() != null ? ud.getSourceConfig().getId() : null;
        return new UnifiedDataResponse(
            ud.getId(),
            sourceConfigId,
            ud.getExternalId(),
            ud.getSourceType(),
            ud.getSourceName(),
            ud.getPayload(),
            ud.getIngestedAt(),
            ud.getUpdatedAt()
        );
    }
}
