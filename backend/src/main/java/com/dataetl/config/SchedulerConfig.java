package com.dataetl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("etl-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.initialize();
        return scheduler;
    }

    /**
     * Exposes the scheduler's underlying executor as a named bean so that
     * manual job triggers (CompletableFuture.runAsync) use the same bounded
     * thread pool as scheduled jobs — instead of the shared ForkJoinPool.commonPool().
     */
    @Bean(name = "jobExecutor")
    public Executor jobExecutor(ThreadPoolTaskScheduler taskScheduler) {
        return taskScheduler.getScheduledExecutor();
    }
}
