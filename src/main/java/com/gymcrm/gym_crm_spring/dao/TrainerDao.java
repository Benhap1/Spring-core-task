package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(TrainerDao.class);
    private final Map<String, Trainer> trainerStorage;

    public TrainerDao(@Qualifier("trainerStorage") Map<String, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
        log.debug("TrainerDao initialized with storage: {}", trainerStorage);
    }

    public void save(Trainer trainer) {
        trainerStorage.put(trainer.getId(), trainer);
        log.debug("Saved trainer id={} name={} {}", trainer.getId(), trainer.getFirstName(), trainer.getLastName());
    }

    public Optional<Trainer> findById(String id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(trainerStorage.values());
    }
}
