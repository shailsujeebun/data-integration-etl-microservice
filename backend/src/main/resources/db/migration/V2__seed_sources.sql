-- =============================================================================
-- V2: Seed default data sources for demo purposes
-- =============================================================================

INSERT INTO source_config (name, type, connection_string, auth_config, schedule_cron, is_active)
VALUES
    (
        'sample-products-csv',
        'CSV',
        '/app/sample-data/sample.csv',
        NULL,
        '0 0/30 * * * ?',
        TRUE
    ),
    (
        'jsonplaceholder-posts',
        'API',
        'https://jsonplaceholder.typicode.com/posts',
        NULL,
        '0 0 * * * ?',
        TRUE
    ),
    (
        'jsonplaceholder-users',
        'API',
        'https://jsonplaceholder.typicode.com/users',
        NULL,
        '0 30 * * * ?',
        TRUE
    );
