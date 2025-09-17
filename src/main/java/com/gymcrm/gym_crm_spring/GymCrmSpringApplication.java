package com.gymcrm.gym_crm_spring;

import com.gymcrm.gym_crm_spring.config.AppConfig;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.facade.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class GymCrmSpringApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade facade = context.getBean(GymFacade.class);

        Trainer trainer = Trainer.builder()
                .firstName("John")
                .lastName("Smith")
                .specialization("Yoga")
                .build();
        trainer = facade.registerTrainer(trainer);
        System.out.println("New Trainer: " + trainer);

        Trainee trainee = Trainee.builder()
                .firstName("Alice")
                .lastName("Brown")
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .build();
        trainee = facade.registerTrainee(trainee);
        System.out.println("New trainee: " + trainee);

        Training training = Training.builder()
                .trainerId(trainer.getId())
                .traineeId(trainee.getId())
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        training = facade.scheduleTraining(training);
        System.out.println("New training: " + training);

        System.out.println("All trainers: " + facade.listTrainers());
        System.out.println("All trainees: " + facade.listTrainees());
        System.out.println("All trainings: " + facade.listTrainings());
    }
}
