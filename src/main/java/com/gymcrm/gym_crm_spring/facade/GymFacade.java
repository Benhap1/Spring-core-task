package com.gymcrm.gym_crm_spring.facade;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.service.TraineeService;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import com.gymcrm.gym_crm_spring.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The Facade encapsulates the work of the services.
 * When creating a Trainer/Trainee profile, we gather all existing users (of both types)
 * and pass them to the service to check for username collisions.
 */

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainer registerTrainer(Trainer trainer) {
        List<User> all = new ArrayList<>();
        all.addAll(trainerService.listAll());
        all.addAll(traineeService.listAll());
        return trainerService.createTrainer(trainer, all);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.update(trainer);
    }

    public Trainee registerTrainee(Trainee trainee) {
        List<User> all = new ArrayList<>();
        all.addAll(trainerService.listAll());
        all.addAll(traineeService.listAll());
        return traineeService.createTrainee(trainee, all);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    public void deleteTrainee(String traineeId) {
        traineeService.delete(traineeId);
    }

    public Training scheduleTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public List<Trainer> listTrainers() { return trainerService.listAll(); }
    public List<Trainee> listTrainees() { return traineeService.listAll(); }
    public List<Training> listTrainings() { return trainingService.listAll(); }
}