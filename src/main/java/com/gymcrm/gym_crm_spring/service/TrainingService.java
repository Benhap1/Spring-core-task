package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainingDao;
import com.gymcrm.gym_crm_spring.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingDao trainingDao;

    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
        log.debug("TrainingService created with TrainingDao");
    }

    public Training createTraining(Training t) {
        Training training = Training.builder()
                .id(UUID.randomUUID().toString())
                .trainerId(t.getTrainerId())
                .traineeId(t.getTraineeId())
                .trainingName(t.getTrainingName())
                .trainingType(t.getTrainingType())
                .trainingDate(t.getTrainingDate())
                .trainingDurationMinutes(t.getTrainingDurationMinutes())
                .build();

        trainingDao.save(training);
        log.info("Created training name={} trainerId={} traineeId={}",
                training.getTrainingName(), training.getTrainerId(), training.getTraineeId());
        return training;
    }

    public List<Training> listAll() {
        return trainingDao.findAll();
    }
}
