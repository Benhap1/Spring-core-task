package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(TrainerDao.class);

    @Qualifier("trainerStorage")
    private final Map<UUID, Trainer> trainerStorage;

    public void save(Trainer trainer) {
        trainerStorage.put(trainer.getId(), trainer);
        log.debug("Saved trainer id={} name={} {}", trainer.getId(), trainer.getFirstName(), trainer.getLastName());
    }

    public Optional<Trainer> findById(UUID id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(trainerStorage.values());
    }
}
