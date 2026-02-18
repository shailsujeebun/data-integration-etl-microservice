package com.dataetl.pipeline.extract;

import com.dataetl.model.SourceConfig;

import java.util.List;
import java.util.Map;

public interface Extractor {

    /**
     * Extracts raw records from the configured source.
     * Returns each record as a Map of field name to value.
     * Supports incremental loading via SourceConfig.lastRunTimestamp.
     *
     * @param config the source configuration
     * @return list of raw records (never null, may be empty)
     * @throws Exception if extraction fails (caller handles retry/error logging)
     */
    List<Map<String, Object>> extract(SourceConfig config) throws Exception;
}
