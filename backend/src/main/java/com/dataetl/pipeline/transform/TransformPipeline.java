package com.dataetl.pipeline.transform;

import com.dataetl.model.SourceConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class TransformPipeline {

    // Ordered list of date patterns to attempt parsing
    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("M/d/yyyy"),
        DateTimeFormatter.ofPattern("d/M/yyyy")
    );

    // Matches strings that look like dates (basic heuristic)
    private static final Pattern DATE_PATTERN =
        Pattern.compile("\\d{1,4}[/\\-]\\d{1,2}[/\\-]\\d{2,4}");

    public record TransformResult(
        Map<String, Object> record,
        String errorReason
    ) {
        public boolean isValid() { return record != null; }
    }

    public TransformResult transform(Map<String, Object> rawRecord, SourceConfig config) {
        // Step 1: Validate
        var validationError = validate(rawRecord);
        if (validationError != null) {
            return new TransformResult(null, validationError);
        }

        // Step 2: Normalize dates
        var normalized = normalizeDates(rawRecord);

        // Step 3: Enrich with metadata
        var enriched = enrich(normalized, config);

        return new TransformResult(enriched, null);
    }

    private String validate(Map<String, Object> record) {
        // Check for at least one identifier field
        boolean hasId = false;
        for (var key : List.of("id", "_id", "uuid", "record_id", "key", "userId")) {
            var value = record.get(key);
            if (value != null && !String.valueOf(value).isBlank()) {
                hasId = true;
                break;
            }
        }
        if (!hasId) {
            return "Missing required identifier field (id, _id, uuid, or record_id)";
        }

        // Check that record is not entirely empty
        var nonBlankValues = record.values().stream()
            .filter(v -> v != null && !String.valueOf(v).isBlank())
            .count();
        if (nonBlankValues <= 1) {
            return "Record contains insufficient data (only identifier field present)";
        }

        return null;
    }

    private Map<String, Object> normalizeDates(Map<String, Object> record) {
        var result = new HashMap<>(record);
        for (var entry : record.entrySet()) {
            var value = entry.getValue();
            if (value instanceof String s && DATE_PATTERN.matcher(s).matches()) {
                var parsed = tryParseDate(s);
                if (parsed != null) {
                    result.put(entry.getKey(), parsed);
                }
            }
        }
        return result;
    }

    private String tryParseDate(String value) {
        for (var formatter : DATE_FORMATTERS) {
            try {
                var date = LocalDate.parse(value.trim(), formatter);
                return date.toString(); // ISO 8601: yyyy-MM-dd
            } catch (DateTimeParseException ignored) {
                // Try next format
            }
        }
        return null; // Could not parse — leave original value
    }

    private Map<String, Object> enrich(Map<String, Object> record, SourceConfig config) {
        var result = new HashMap<>(record);
        result.put("_source_name", config.getName());
        result.put("_source_type", config.getType().name());
        result.put("_ingested_at", OffsetDateTime.now().toString());
        return result;
    }
}
