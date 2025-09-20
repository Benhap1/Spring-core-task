package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TrainingDao {
    private static final Logger log = LoggerFactory.getLogger(TrainingDao.class);

    @Qualifier("trainingStorage")
    private final Map<UUID, Training> trainingStorage;

    public void save(Training training) {
        if (training.getId() == null) {
            training.setId(UUID.randomUUID());
        }
        trainingStorage.put(training.getId(), training);
        log.debug("Saved training id={} name={}", training.getId(), training.getTrainingName());
    }

    public Optional<Training> findById(UUID id) {
        return Optional.ofNullable(trainingStorage.get(id));
    }

    public List<Training> findAll() {
        return new ArrayList<>(trainingStorage.values());
    }

    public List<Training> findByTrainerId(UUID trainerId) {
        return findAll().stream()
                .filter(t -> trainerId.equals(t.getTrainerId()))
                .toList();
    }
}
