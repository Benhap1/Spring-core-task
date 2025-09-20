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
import java.util.UUID;

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

        if (!("trainerStorage".equals(beanName) || "traineeStorage".equals(beanName) || "trainingStorage".equals(beanName))) {
            return bean;
        }

        try (InputStream is = initialData.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            switch (beanName) {
                case "trainerStorage" -> {
                    Map<UUID, Trainer> storage = (Map<UUID, Trainer>) bean;
                    JsonNode trainers = root.path("trainers");
                    if (trainers.isArray()) {
                        for (JsonNode t : trainers) {
                            UUID id = parseUUID(t.path("id").asText(null));
                            Trainer trainer = Trainer.builder()
                                    .id(id)
                                    .firstName(t.path("firstName").asText(null))
                                    .lastName(t.path("lastName").asText(null))
                                    .specialization(t.path("specialization").asText(null))
                                    .build();
                            storage.put(trainer.getId(), trainer);
                        }
                    }
                    log.info("Initialized trainerStorage with {} entries", storage.size());
                }
                case "traineeStorage" -> {
                    Map<UUID, Trainee> storage = (Map<UUID, Trainee>) bean;
                    JsonNode trainees = root.path("trainees");
                    if (trainees.isArray()) {
                        for (JsonNode t : trainees) {
                            UUID id = parseUUID(t.path("id").asText(null));
                            Trainee trainee = Trainee.builder()
                                    .id(id)
                                    .firstName(t.path("firstName").asText(null))
                                    .lastName(t.path("lastName").asText(null))
                                    .dateOfBirth(parseLocalDate(t.path("dateOfBirth").asText(null)))
                                    .address(t.path("address").asText(null))
                                    .build();
                            storage.put(trainee.getId(), trainee);
                        }
                    }
                    log.info("Initialized traineeStorage with {} entries", storage.size());
                }
                case "trainingStorage" -> {
                    Map<UUID, Training> storage = (Map<UUID, Training>) bean;
                    JsonNode trainings = root.path("trainings");
                    if (trainings.isArray()) {
                        for (JsonNode tn : trainings) {
                            UUID id = parseUUID(tn.path("id").asText(null));
                            UUID trainerId = parseUUID(tn.path("trainerId").asText(null));
                            UUID traineeId = parseUUID(tn.path("traineeId").asText(null));
                            Training training = Training.builder()
                                    .id(id)
                                    .trainerId(trainerId)
                                    .traineeId(traineeId)
                                    .trainingName(tn.path("trainingName").asText(null))
                                    .trainingType(tn.path("trainingType").asText(null))
                                    .trainingDate(parseLocalDate(tn.path("trainingDate").asText(null)))
                                    .trainingDurationMinutes(tn.path("trainingDurationMinutes").asInt(0))
                                    .build();
                            storage.put(training.getId(), training);
                        }
                    }
                    log.info("Initialized trainingStorage with {} entries", storage.size());
                }
            }

        } catch (Exception e) {
            log.warn("Failed to initialize storage {} from {}: {}", beanName, initialData, e.getMessage(), e);
        }

        return bean;
    }

    private UUID parseUUID(String text) {
        if (text == null || text.isEmpty()) {
            return UUID.randomUUID();
        }
        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid UUID '{}' in initial data — generating new one", text);
            return UUID.randomUUID();
        }
    }

    private LocalDate parseLocalDate(String date) {
        return (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
    }
}
