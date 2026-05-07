package com.vendor.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

/**
 * Application-wide configuration beans.
 *
 * Provides:
 * - RestTemplate for calling the Flask AI microservice
 * - AsyncExecutor thread pool for background AI processing
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate bean for HTTP calls to the Flask AI service.
     * Shared across the application to avoid creating multiple instances.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Custom async executor bean used by @Async methods.
     * Configured with bounded pool size to avoid resource exhaustion.
     */
    @Bean(name = "aiTaskExecutor")
    public Executor aiTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AI-Task-");
        executor.initialize();
        return executor;
    }
}
