package com.dataetl.pipeline.extract;

import com.dataetl.model.SourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class ApiExtractor implements Extractor {

    private static final Logger log = LoggerFactory.getLogger(ApiExtractor.class);

    private final WebClient webClient;

    public ApiExtractor(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Map<String, Object>> extract(SourceConfig config) throws Exception {
        // Security: block SSRF — reject private IPs, loopback, link-local, metadata endpoints
        SsrfGuard.assertSafeUrl(config.getConnectionString());

        var uriBuilder = UriComponentsBuilder.fromUriString(config.getConnectionString());

        // Incremental loading: append 'since' parameter if this source has run before
        if (config.getLastRunTimestamp() != null) {
            uriBuilder.queryParam("since", config.getLastRunTimestamp().toInstant().toString());
        }

        var uri = uriBuilder.build().toUri();

        // V9: Log only the host+path, never query params (may contain API keys in ?api_key=... style URLs)
        log.info("{\"event\":\"api_extract_start\",\"source\":\"{}\",\"host\":\"{}\",\"path\":\"{}\"}",
            config.getName(), uri.getHost(), uri.getPath());

        var requestSpec = webClient.get().uri(uri);

        // Apply authentication from authConfig JSONB
        // Supports: {"type":"bearer","token":"..."} and {"type":"apikey","header":"X-API-Key","value":"..."}
        if (config.getAuthConfig() != null) {
            var authType = String.valueOf(config.getAuthConfig().get("type"));
            if ("bearer".equals(authType)) {
                var token = String.valueOf(config.getAuthConfig().get("token"));
                requestSpec = requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            } else if ("apikey".equals(authType)) {
                var header = String.valueOf(config.getAuthConfig().get("header"));
                var value = String.valueOf(config.getAuthConfig().get("value"));
                requestSpec = requestSpec.header(header, value);
            }
        }

        var response = requestSpec
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                .doBeforeRetry(sig -> log.warn("{\"event\":\"api_retry\",\"source\":\"{}\",\"attempt\":{}}",
                    config.getName(), sig.totalRetriesInARow() + 1)))
            .block(Duration.ofSeconds(30));

        var records = response != null ? response : List.<Map<String, Object>>of();
        log.info("{\"event\":\"api_extract_done\",\"source\":\"{}\",\"records\":{}}",
            config.getName(), records.size());
        return records;
    }
}
