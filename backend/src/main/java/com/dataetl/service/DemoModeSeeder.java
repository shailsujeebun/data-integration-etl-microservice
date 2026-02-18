package com.dataetl.service;

import com.dataetl.model.SourceConfig;
import com.dataetl.model.enums.SourceType;
import com.dataetl.repository.SourceConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("demo")
public class DemoModeSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoModeSeeder.class);

    private final SourceConfigRepository sourceConfigRepository;
    private final DynamicSchedulerService schedulerService;

    public DemoModeSeeder(SourceConfigRepository sourceConfigRepository,
                          DynamicSchedulerService schedulerService) {
        this.sourceConfigRepository = sourceConfigRepository;
        this.schedulerService = schedulerService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        upsertSource("sample-products-csv", SourceType.CSV, "/app/sample-data/sample.csv", "0 0/2 * * * ?");
        upsertSource("jsonplaceholder-posts", SourceType.API, "https://jsonplaceholder.typicode.com/posts", "0 0/3 * * * ?");
        upsertSource("jsonplaceholder-users", SourceType.API, "https://jsonplaceholder.typicode.com/users", "0 0/5 * * * ?");
        log.info("{\"event\":\"demo_mode_seed_applied\"}");
    }

    private void upsertSource(String name, SourceType type, String connectionString, String cron) {
        var source = sourceConfigRepository.findByName(name).orElseGet(SourceConfig::new);
        source.setName(name);
        source.setType(type);
        source.setConnectionString(connectionString);
        source.setAuthConfig(null);
        source.setScheduleCron(cron);
        source.setIsActive(true);

        var saved = sourceConfigRepository.save(source);
        schedulerService.scheduleSource(saved);
    }
}
