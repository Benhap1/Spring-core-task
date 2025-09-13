package com.gymcrm.gym_crm_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan(basePackages = "com.gymcrm.gym_crm_spring")
public class AppConfig {

    @Bean(name = "trainerStorage")
    public Map<String, Object> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "traineeStorage")
    public Map<String, Object> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<String, Object> trainingStorage() {
        return new ConcurrentHashMap<>();
    }
}