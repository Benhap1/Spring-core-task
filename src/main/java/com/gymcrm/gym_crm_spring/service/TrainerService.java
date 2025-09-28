package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.springframework.stereotype.Service;

@Service
public class TrainerService extends AbstractService<Trainer> {
    public TrainerService(TrainerDao dao) {
        super(dao);
    }
}


