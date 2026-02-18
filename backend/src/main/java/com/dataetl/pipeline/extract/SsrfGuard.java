package com.dataetl.pipeline.extract;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Prevents Server-Side Request Forgery (SSRF) attacks.
 *
 * Without this guard, an attacker who can call POST /api/sources could set
 * connectionString to http://169.254.169.254/latest/meta-data/ (AWS metadata),
 * http://internal-db:5432/, or file:///etc/passwd — using the backend as a
 * proxy to reach internal infrastructure.
 *
 * Usage: call SsrfGuard.assertSafeUrl(url) before making any outbound request.
 * Throws IllegalArgumentException (handled by GlobalExceptionHandler → 400) on violation.
 */
public final class SsrfGuard {

    private SsrfGuard() {}

    // Schemes we allow for API sources
    private static final List<String> ALLOWED_HTTP_SCHEMES = List.of("http", "https");

    // Allowed schemes for CSV sources (local file path via file://)
    // We allow it but restrict the path prefix to the app's sample-data directory.
    private static final String ALLOWED_FILE_PREFIX = "/app/sample-data/";

    // RFC-1918 private ranges + loopback + link-local + metadata endpoints
    private static final List<Pattern> BLOCKED_HOST_PATTERNS = List.of(
        Pattern.compile("^localhost$", Pattern.CASE_INSENSITIVE),
        Pattern.compile("^127\\..*"),                       // 127.x.x.x loopback
        Pattern.compile("^10\\..*"),                        // 10.0.0.0/8
        Pattern.compile("^172\\.(1[6-9]|2[0-9]|3[01])\\..*"), // 172.16.0.0/12
        Pattern.compile("^192\\.168\\..*"),                 // 192.168.0.0/16
        Pattern.compile("^169\\.254\\..*"),                 // link-local / AWS metadata
        Pattern.compile("^::1$"),                           // IPv6 loopback
        Pattern.compile("^fc.*"),                           // IPv6 unique local
        Pattern.compile("^fe80.*")                          // IPv6 link-local
    );

    /**
     * Validates that a URL is safe to fetch (HTTP/HTTPS only, no private IPs).
     */
    public static void assertSafeUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Connection URL must not be blank");
        }

        URI uri;
        try {
            uri = URI.create(url.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Connection URL is malformed: " + url);
        }

        var scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "";

        if (!ALLOWED_HTTP_SCHEMES.contains(scheme)) {
            throw new IllegalArgumentException(
                "Connection URL scheme '" + scheme + "' is not permitted. Only http and https are allowed for API sources."
            );
        }

        var host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Connection URL must contain a valid hostname");
        }

        // Check against known private/internal hostname patterns
        for (var pattern : BLOCKED_HOST_PATTERNS) {
            if (pattern.matcher(host).matches()) {
                throw new IllegalArgumentException(
                    "Connection URL targets a private or internal address '" + host + "' which is not permitted"
                );
            }
        }

        // DNS resolution check — catches hostnames that resolve to private IPs
        try {
            var addresses = InetAddress.getAllByName(host);
            for (var addr : addresses) {
                if (addr.isLoopbackAddress() || addr.isSiteLocalAddress() || addr.isLinkLocalAddress()) {
                    throw new IllegalArgumentException(
                        "Connection URL hostname '" + host + "' resolves to a private address which is not permitted"
                    );
                }
            }
        } catch (UnknownHostException e) {
            // Unknown host — let the actual request fail naturally with a cleaner error
        }
    }

    /**
     * Validates that a file path for a CSV source stays within the allowed directory.
     * Prevents path traversal (e.g., ../../etc/passwd).
     */
    public static void assertSafeFilePath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }
        // Normalize the path to resolve any ../ traversal attempts
        var normalized = java.nio.file.Path.of(path).normalize().toString()
            .replace("\\", "/");
        if (!normalized.startsWith(ALLOWED_FILE_PREFIX)) {
            throw new IllegalArgumentException(
                "CSV file path must be within " + ALLOWED_FILE_PREFIX +
                ". Path traversal and absolute paths outside this directory are not permitted."
            );
        }
    }
}
