package com.dataetl.pipeline.extract;

import com.dataetl.model.SourceConfig;
import com.opencsv.CSVReaderHeaderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvExtractor implements Extractor {

    private static final Logger log = LoggerFactory.getLogger(CsvExtractor.class);

    @Override
    public List<Map<String, Object>> extract(SourceConfig config) throws Exception {
        var filePath = config.getConnectionString();

        // Security: block path traversal (e.g., ../../etc/passwd).
        // File must reside within the allowed sample-data directory.
        SsrfGuard.assertSafeFilePath(filePath);

        log.info("{\"event\":\"csv_extract_start\",\"source\":\"{}\"}", config.getName());

        var records = new ArrayList<Map<String, Object>>();

        try (var reader = new CSVReaderHeaderAware(new FileReader(filePath))) {
            Map<String, String> row;
            while ((row = reader.readMap()) != null) {
                // Copy to Map<String, Object> for uniform handling downstream
                records.add(new HashMap<>(row));
            }
        }

        log.info("{\"event\":\"csv_extract_done\",\"source\":\"{}\",\"records\":{}}", config.getName(), records.size());
        return records;
    }
}
