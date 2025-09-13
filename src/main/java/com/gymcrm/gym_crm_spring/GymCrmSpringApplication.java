package com.gymcrm.gym_crm_spring;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.facade.GymFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class GymCrmSpringApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(GymCrmSpringApplication.class, args);

        GymFacade facade = context.getBean(GymFacade.class);

        Trainer trainer = Trainer.builder()
                .firstName("John")
                .lastName("Smith")
                .specialization("Yoga")
                .build();
        trainer = facade.registerTrainer(trainer);
        System.out.println("Создан тренер: " + trainer);

        Trainee trainee = Trainee.builder()
                .firstName("Alice")
                .lastName("Brown")
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .build();
        trainee = facade.registerTrainee(trainee);
        System.out.println("Создан ученик: " + trainee);

        Training training = Training.builder()
                .trainerId(trainer.getId())
                .traineeId(trainee.getId())
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        training = facade.scheduleTraining(training);
        System.out.println("Создана тренировка: " + training);

        System.out.println("Все тренеры: " + facade.listTrainers());
        System.out.println("Все ученики: " + facade.listTrainees());
        System.out.println("Все тренировки: " + facade.listTrainings());
    }
}
