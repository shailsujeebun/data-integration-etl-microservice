package com.dataetl.dto;

import com.dataetl.model.ErrorLog;
import com.dataetl.model.JobHistory;

import java.util.List;

public record JobDetailResponse(
    JobHistoryResponse job,
    List<ErrorLogResponse> errors
) {
    public static JobDetailResponse from(JobHistory jh, List<ErrorLog> errors) {
        return new JobDetailResponse(
            JobHistoryResponse.from(jh),
            errors.stream().map(ErrorLogResponse::from).toList()
        );
    }
}
