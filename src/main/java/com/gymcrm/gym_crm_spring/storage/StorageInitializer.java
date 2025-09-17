package com.gymcrm.gym_crm_spring.storage;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;


@Component
public class StorageInitializer implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${initial.data.file}")
    private Resource initialData;

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (!(bean instanceof Map)) {
            return bean;
        }

        try (InputStream is = initialData.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            switch (beanName) {
                case "trainerStorage" -> {
                    Map<String, Trainer> storage = (Map<String, Trainer>) bean;
                    JsonNode trainers = root.path("trainers");
                    for (JsonNode t : trainers) {
                        Trainer trainer = Trainer.builder()
                                .id(t.path("id").asText())
                                .firstName(t.path("firstName").asText())
                                .lastName(t.path("lastName").asText())
                                .specialization(t.path("specialization").asText())
                                .build();
                        storage.put(trainer.getId(), trainer);
                    }
                    log.info("Initialized trainerStorage with {} entries", storage.size());
                }
                case "traineeStorage" -> {
                    Map<String, Trainee> storage = (Map<String, Trainee>) bean;
                    JsonNode trainees = root.path("trainees");
                    for (JsonNode t : trainees) {
                        Trainee trainee = Trainee.builder()
                                .id(t.path("id").asText())
                                .firstName(t.path("firstName").asText())
                                .lastName(t.path("lastName").asText())
                                .dateOfBirth(parseLocalDate(t.path("dateOfBirth").asText(null)))
                                .address(t.path("address").asText(null))
                                .build();
                        storage.put(trainee.getId(), trainee);
                    }
                    log.info("Initialized traineeStorage with {} entries", storage.size());
                }
                case "trainingStorage" -> {
                    Map<String, Training> storage = (Map<String, Training>) bean;
                    JsonNode trainings = root.path("trainings");
                    for (JsonNode tn : trainings) {
                        Training training = Training.builder()
                                .id(tn.path("id").asText())
                                .trainerId(tn.path("trainerId").asText())
                                .traineeId(tn.path("traineeId").asText())
                                .trainingName(tn.path("trainingName").asText())
                                .trainingType(tn.path("trainingType").asText())
                                .trainingDate(parseLocalDate(tn.path("trainingDate").asText(null)))
                                .trainingDurationMinutes(tn.path("trainingDurationMinutes").asInt(0))
                                .build();
                        storage.put(training.getId(), training);
                    }
                    log.info("Initialized trainingStorage with {} entries", storage.size());
                }
            }

        } catch (Exception e) {
            log.warn("Failed to initialize storage {} from {}: {}", beanName, initialData, e.getMessage());
        }

        return bean;
    }

    private LocalDate parseLocalDate(String date) {
        return (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
    }
}
