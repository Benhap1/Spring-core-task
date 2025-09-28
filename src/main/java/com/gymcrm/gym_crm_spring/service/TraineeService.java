package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TraineeDao;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import org.springframework.stereotype.Service;

@Service
public class TraineeService extends AbstractService<Trainee> {
    public TraineeService(TraineeDao dao) {
        super(dao);
    }
}


