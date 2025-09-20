package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDao.class);

    @Qualifier("traineeStorage")
    private final Map<UUID, Trainee> traineeStorage;

    public void save(Trainee trainee) {
        traineeStorage.put(trainee.getId(), trainee);
        log.debug("Saved trainee id={} name={} {}", trainee.getId(), trainee.getFirstName(), trainee.getLastName());
    }

    public void delete(UUID id) {
        traineeStorage.remove(id);
        log.debug("Deleted trainee id={}", id);
    }

    public Optional<Trainee> findById(UUID id) {
        return Optional.ofNullable(traineeStorage.get(id));
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(traineeStorage.values());
    }
}
