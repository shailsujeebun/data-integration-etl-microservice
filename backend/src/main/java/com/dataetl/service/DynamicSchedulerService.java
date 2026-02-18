package com.dataetl.service;

import com.dataetl.model.SourceConfig;
import com.dataetl.model.enums.TriggerType;
import com.dataetl.pipeline.EtlOrchestrator;
import com.dataetl.repository.SourceConfigRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(DynamicSchedulerService.class);

    private final TaskScheduler taskScheduler;
    private final EtlOrchestrator orchestrator;
    private final SourceConfigRepository sourceConfigRepository;

    // ConcurrentHashMap is required: multiple threads can interact with this map
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public DynamicSchedulerService(TaskScheduler taskScheduler,
                                   EtlOrchestrator orchestrator,
                                   SourceConfigRepository sourceConfigRepository) {
        this.taskScheduler = taskScheduler;
        this.orchestrator = orchestrator;
        this.sourceConfigRepository = sourceConfigRepository;
    }

    @PostConstruct
    public void initializeSchedules() {
        var activeSources = sourceConfigRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        for (var source : activeSources) {
            scheduleSource(source);
        }
        log.info("{\"event\":\"scheduler_init\",\"scheduledCount\":{}}", scheduledTasks.size());
    }

    public void scheduleSource(SourceConfig config) {
        // Cancel existing task for this source before re-registering
        cancelSchedule(config.getId());

        if (config.getScheduleCron() == null || config.getScheduleCron().isBlank()) {
            return;
        }
        if (!Boolean.TRUE.equals(config.getIsActive())) {
            return;
        }

        try {
            var trigger = new CronTrigger(config.getScheduleCron());
            var future = taskScheduler.schedule(
                () -> {
                    log.info("{\"event\":\"scheduled_trigger\",\"source\":\"{}\"}",config.getName());
                    orchestrator.runJob(config.getId(), TriggerType.SCHEDULER);
                },
                trigger
            );
            scheduledTasks.put(config.getId(), future);
            log.info("{\"event\":\"source_scheduled\",\"source\":\"{}\",\"cron\":\"{}\"}",
                config.getName(), config.getScheduleCron());
        } catch (IllegalArgumentException e) {
            log.error("{\"event\":\"invalid_cron\",\"source\":\"{}\",\"cron\":\"{}\",\"error\":\"{}\"}",
                config.getName(), config.getScheduleCron(), e.getMessage());
        }
    }

    public void cancelSchedule(Long sourceConfigId) {
        var existing = scheduledTasks.remove(sourceConfigId);
        if (existing != null) {
            existing.cancel(false); // false = don't interrupt if currently running
        }
    }

    public int getScheduledCount() {
        return scheduledTasks.size();
    }
}
