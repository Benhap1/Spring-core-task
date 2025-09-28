package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerService extends AbstractService<Trainer> {
    private final TrainerDao dao;

    public TrainerService(TrainerDao dao) {
        super(dao);
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Optional<Trainer> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Trainer> findNotAssignedToTrainee(UUID traineeId) {
        return dao.findNotAssignedToTrainee(traineeId);
    }
}
