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

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@Component
public class StorageInitializer implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${initial.data.file}")
    private Resource initialData;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Initialization only if the bean is a Map and its name matches one of the storages
        if (!(bean instanceof Map)) {
            return bean;
        }

        if (!("trainerStorage".equals(beanName) || "traineeStorage".equals(beanName) || "trainingStorage".equals(beanName))) {
            return bean;
        }

        try (InputStream is = initialData.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            if ("trainerStorage".equals(beanName)) {
                JsonNode trainers = root.path("trainers");
                for (JsonNode t : trainers) {
                    Trainer trainer = Trainer.builder()
                            .id(t.path("id").asText())
                            .firstName(t.path("firstName").asText())
                            .lastName(t.path("lastName").asText())
                            .specialization(t.path("specialization").asText())
                            .build();
                    ((Map<String, Object>) bean).put(trainer.getId(), trainer);
                }
                log.info("Initialized trainerStorage with {} entries", ((Map<?, ?>) bean).size());

            } else if ("traineeStorage".equals(beanName)) {
                JsonNode trainees = root.path("trainees");
                for (JsonNode t : trainees) {
                    Trainee trainee = Trainee.builder()
                            .id(t.path("id").asText())
                            .firstName(t.path("firstName").asText())
                            .lastName(t.path("lastName").asText())
                            .dateOfBirth(parseLocalDate(t.path("dateOfBirth").asText(null)))
                            .address(t.path("address").asText(null))
                            .build();
                    ((Map<String, Object>) bean).put(trainee.getId(), trainee);
                }
                log.info("Initialized traineeStorage with {} entries", ((Map<?, ?>) bean).size());

            } else if ("trainingStorage".equals(beanName)) {
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
                    ((Map<String, Object>) bean).put(training.getId(), training);
                }
                log.info("Initialized trainingStorage with {} entries", ((Map<?, ?>) bean).size());
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
