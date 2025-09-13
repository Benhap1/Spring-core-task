package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDao.class);
    private final Map<String, Object> traineeStorage;

    public TraineeDao(Map<String, Object> traineeStorage) {
        this.traineeStorage = traineeStorage;
        log.debug("TraineeDao initialized with storage: {}", traineeStorage);
    }

    public void save(Trainee trainee) {
        traineeStorage.put(trainee.getId(), trainee);
        log.debug("Saved trainee id={} name={} {}", trainee.getId(), trainee.getFirstName(), trainee.getLastName());
    }

    public void delete(String id) {
        traineeStorage.remove(id);
        log.debug("Deleted trainee id={}", id);
    }

    public Optional<Trainee> findById(String id) {
        return Optional.ofNullable((Trainee) traineeStorage.get(id));
    }

    public List<Trainee> findAll() {
        return traineeStorage.values().stream().map(o -> (Trainee) o).collect(Collectors.toList());
    }
}