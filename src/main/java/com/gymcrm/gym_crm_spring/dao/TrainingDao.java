package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TrainingDao {
    private static final Logger log = LoggerFactory.getLogger(TrainingDao.class);
    private final Map<String, Object> trainingStorage;

    public TrainingDao(Map<String, Object> trainingStorage) {
        this.trainingStorage = trainingStorage;
        log.debug("TrainingDao initialized with storage: {}", trainingStorage);
    }

    public void save(Training training) {
        if (training.getId() == null) {
            training.setId(UUID.randomUUID().toString());
        }
        trainingStorage.put(training.getId(), training);
        log.debug("Saved training id={} name={}", training.getId(), training.getTrainingName());
    }

    public Optional<Training> findById(String id) {
        return Optional.ofNullable((Training) trainingStorage.get(id));
    }

    public List<Training> findAll() {
        return trainingStorage.values().stream().map(o -> (Training) o).collect(Collectors.toList());
    }

    public List<Training> findByTrainerId(String trainerId) {
        return findAll().stream().filter(t -> trainerId.equals(t.getTrainerId())).collect(Collectors.toList());
    }
}