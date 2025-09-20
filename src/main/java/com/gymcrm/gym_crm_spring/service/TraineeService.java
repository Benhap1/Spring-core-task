package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TraineeDao;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeDao traineeDao;

    public Trainee createTrainee(Trainee t, List<? extends User> existingUsersForCollision) {
        log.info("Creating trainee: {} {}", t.getFirstName(), t.getLastName());

        Trainee trainee = Trainee.builder()
                .id(UUID.randomUUID())
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .dateOfBirth(t.getDateOfBirth())
                .address(t.getAddress())
                .username(UserUtils.generateUsername(t.getFirstName(), t.getLastName(), existingUsersForCollision))
                .password(UserUtils.generatePassword())
                .active(true)
                .build();

        traineeDao.save(trainee);
        log.info("Trainee created id={} username={}", trainee.getId(), trainee.getUsername());
        return trainee;
    }

    public Optional<Trainee> findById(UUID id) {
        return traineeDao.findById(id);
    }

    public List<Trainee> listAll() {
        return traineeDao.findAll();
    }

    public Trainee update(Trainee t) {
        traineeDao.save(t);
        log.info("Updated trainee id={}", t.getId());
        return t;
    }

    public void delete(UUID id) {
        traineeDao.delete(id);
        log.info("Deleted trainee id={}", id);
    }
}

