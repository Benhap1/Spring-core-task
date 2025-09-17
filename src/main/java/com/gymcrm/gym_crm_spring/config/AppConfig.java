package com.gymcrm.gym_crm_spring.config;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import org.springframework.context.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan(basePackages = "com.gymcrm.gym_crm_spring")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean(name = "traineeStorage")
    public Map<String, Trainee> traineeStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<String, Trainer> trainerStorage() {
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<String, Training> trainingStorage() {
        return new ConcurrentHashMap<>();
    }
}
