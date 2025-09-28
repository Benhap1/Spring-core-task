package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TraineeDao;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TraineeService extends AbstractService<Trainee> {
    private final TraineeDao dao;

    public TraineeService(TraineeDao dao) {
        super(dao);
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Optional<Trainee> findByUsername(String username) {
        return dao.findByUsername(username);
    }
}

