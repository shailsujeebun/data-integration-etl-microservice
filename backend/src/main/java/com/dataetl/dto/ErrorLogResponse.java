package com.dataetl.dto;

import com.dataetl.model.ErrorLog;

import java.time.OffsetDateTime;

public record ErrorLogResponse(
    Long id,
    String rawData,
    String errorReason,
    OffsetDateTime occurredAt
) {
    public static ErrorLogResponse from(ErrorLog el) {
        return new ErrorLogResponse(
            el.getId(),
            el.getRawData(),
            el.getErrorReason(),
            el.getOccurredAt()
        );
    }
}
