package com.example.Ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Core pool size
        executor.setMaxPoolSize(20); // Maximum pool size
        executor.setQueueCapacity(500); // Queue capacity
        executor.setThreadNamePrefix("Async-"); // Thread name prefix
        executor.initialize();
        return executor;
    }

}
