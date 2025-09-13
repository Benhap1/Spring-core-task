package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;

    public TrainerService() {
        log.debug("TrainerService created");
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
        log.debug("TrainerDao injected into TrainerService");
    }

    public Trainer createTrainer(Trainer t, List<? extends User> existingUsersForCollision) {
        log.info("Creating trainer: {} {}", t.getFirstName(), t.getLastName());

        String id = UUID.randomUUID().toString();
        String username = UserUtils.generateUsername(t.getFirstName(), t.getLastName(), existingUsersForCollision);
        String password = UserUtils.generatePassword();

        Trainer trainer = Trainer.builder()
                .id(id)
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .specialization(t.getSpecialization())
                .username(username)
                .password(password)
                .active(true)
                .build();

        trainerDao.save(trainer);
        log.info("Trainer created id={} username={}", trainer.getId(), trainer.getUsername());
        return trainer;
    }

    public Optional<Trainer> findById(String id) {
        return trainerDao.findById(id);
    }

    public List<Trainer> listAll() {
        return trainerDao.findAll();
    }

    public Trainer update(Trainer trainer) {
        trainerDao.save(trainer);
        log.info("Updated trainer id={}", trainer.getId());
        return trainer;
    }
}
