package com.gymcrm.gym_crm_spring.config;

import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ch.qos.logback.classic.Level;

@Configuration
@Profile("dev")
public class DevLoggingConfig {
    @PostConstruct
    public void setup() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("com.gymcrm.gym_crm_spring").setLevel(Level.DEBUG);
        context.getLogger("org.hibernate.SQL").setLevel(Level.DEBUG);
    }
}
