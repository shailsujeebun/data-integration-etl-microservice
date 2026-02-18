package com.dataetl.pipeline.extract;

import com.dataetl.model.SourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbExtractor implements Extractor {

    private static final Logger log = LoggerFactory.getLogger(DbExtractor.class);

    @Override
    public List<Map<String, Object>> extract(SourceConfig config) throws Exception {
        var jdbcUrl = config.getConnectionString();

        // Query is stored in authConfig: {"query": "SELECT id, name, ... FROM table"}
        if (config.getAuthConfig() == null || !config.getAuthConfig().containsKey("query")) {
            throw new IllegalArgumentException("DB source requires 'query' field in authConfig");
        }

        var query = (String) config.getAuthConfig().get("query");

        // Security: only SELECT statements are permitted.
        // This prevents stored SQL injection via authConfig (e.g., DROP TABLE, COPY TO STDOUT).
        var trimmedUpper = query.trim().toUpperCase();
        if (!trimmedUpper.startsWith("SELECT")) {
            throw new IllegalArgumentException(
                "DB source query must be a SELECT statement. Only read operations are permitted."
            );
        }
        // Reject statement chaining — semicolons outside of string literals allow DROP, INSERT, etc.
        if (query.contains(";")) {
            throw new IllegalArgumentException(
                "DB source query must not contain semicolons (statement chaining is not permitted)"
            );
        }

        // Incremental loading: append timestamp filter if previous run exists.
        // Timestamp is bound as a PreparedStatement parameter — NOT string-concatenated.
        String finalQuery = query;
        boolean hasWhere = trimmedUpper.contains("WHERE");
        if (config.getLastRunTimestamp() != null) {
            finalQuery += hasWhere ? " AND updated_at > ?" : " WHERE updated_at > ?";
        }

        log.info("{\"event\":\"db_extract_start\",\"source\":\"{}\",\"hasTimestampFilter\":{}}",
            config.getName(), config.getLastRunTimestamp() != null);

        var records = new ArrayList<Map<String, Object>>();

        try (var conn = DriverManager.getConnection(jdbcUrl);
             var stmt = conn.prepareStatement(finalQuery)) {

            if (config.getLastRunTimestamp() != null) {
                stmt.setString(1, config.getLastRunTimestamp().toInstant().toString());
            }

            try (var rs = stmt.executeQuery()) {
                var meta = rs.getMetaData();
                var colCount = meta.getColumnCount();

                while (rs.next()) {
                    var row = new LinkedHashMap<String, Object>();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    records.add(row);
                }
            }
        }

        log.info("{\"event\":\"db_extract_done\",\"source\":\"{}\",\"records\":{}}",
            config.getName(), records.size());
        return records;
    }
}
