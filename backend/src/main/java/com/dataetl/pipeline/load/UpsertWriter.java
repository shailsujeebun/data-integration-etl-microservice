package com.dataetl.pipeline.load;

import com.dataetl.model.SourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UpsertWriter {

    private static final Logger log = LoggerFactory.getLogger(UpsertWriter.class);
    private static final int BATCH_SIZE = 500;

    @PersistenceContext
    private EntityManager entityManager;

    private final ObjectMapper objectMapper;

    public UpsertWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void upsertBatch(List<Map<String, Object>> records, SourceConfig config) {
        int count = 0;
        for (var record : records) {
            var externalId = extractExternalId(record);
            try {
                var payloadJson = objectMapper.writeValueAsString(record);

                entityManager.createNativeQuery("""
                    INSERT INTO unified_data
                        (source_config_id, external_id, source_type, source_name, payload, ingested_at, updated_at)
                    VALUES
                        (:sourceId, :extId, :sourceType, :sourceName, CAST(:payload AS jsonb), NOW(), NOW())
                    ON CONFLICT (source_config_id, external_id)
                    DO UPDATE SET
                        payload = CAST(EXCLUDED.payload AS jsonb),
                        updated_at = NOW()
                    """)
                    .setParameter("sourceId", config.getId())
                    .setParameter("extId", externalId)
                    .setParameter("sourceType", config.getType().name())
                    .setParameter("sourceName", config.getName())
                    .setParameter("payload", payloadJson)
                    .executeUpdate();

                count++;

                // Flush and clear Hibernate's L1 cache every BATCH_SIZE records
                // This is critical to prevent OOM with large datasets (e.g., 100k records)
                if (count % BATCH_SIZE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                    log.info("{\"event\":\"batch_flush\",\"source\":\"{}\",\"flushed\":{}}",
                        config.getName(), count);
                }

            } catch (Exception e) {
                log.error("{\"event\":\"upsert_error\",\"source\":\"{}\",\"externalId\":\"{}\",\"error\":\"{}\"}",
                    config.getName(), externalId, e.getMessage());
                throw new RuntimeException("Upsert failed for record " + externalId + ": " + e.getMessage(), e);
            }
        }

        // Final flush for remaining records
        entityManager.flush();
        entityManager.clear();

        log.info("{\"event\":\"upsert_complete\",\"source\":\"{}\",\"total\":{}}", config.getName(), count);
    }

    private String extractExternalId(Map<String, Object> record) {
        // Try common identifier field names in priority order
        for (var key : List.of("id", "_id", "uuid", "record_id", "userId", "key")) {
            var value = record.get(key);
            if (value != null && !String.valueOf(value).isBlank()) {
                return String.valueOf(value);
            }
        }
        // Fallback: UUID v4 — Java's hashCode() is only 32-bit (~4B values) and can silently
        // collide, causing records to overwrite each other. UUID is effectively collision-free.
        // Note: each run of the same record without an ID field will generate a new UUID,
        // meaning it inserts a new row rather than upsert. This is intentional — without an
        // identifier we cannot determine if two records represent the same entity.
        return UUID.randomUUID().toString();
    }
}
