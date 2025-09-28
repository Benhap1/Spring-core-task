package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainingDao;
import com.gymcrm.gym_crm_spring.domain.Training;
import org.springframework.stereotype.Service;

@Service
public class TrainingService extends AbstractService<Training> {
    public TrainingService(TrainingDao dao) {
        super(dao);
    }
}


