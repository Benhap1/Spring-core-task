package com.gymcrm.gym_crm_spring;

import com.gymcrm.gym_crm_spring.config.AppConfig;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.domain.TrainingType;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.facade.GymFacade;
import com.gymcrm.gym_crm_spring.service.TrainingTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;


public class GymCrmSpringApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade facade = context.getBean(GymFacade.class);
        TrainingTypeService trainingTypeService = context.getBean(TrainingTypeService.class);

        // Get training type
        TrainingType yoga = trainingTypeService.findByName("Yoga")
                .orElseThrow(() -> new RuntimeException("TrainingType 'Yoga' not found in DB!"));

        // Create trainer
        Trainer trainer = Trainer.builder()
                .user(User.builder().firstName("John").lastName("Smith").active(true).build())
                .specialization(yoga)
                .build();
        var regTrainer = facade.registerTrainerWithPassword(trainer);
        trainer = regTrainer.entity();
        String trainerPass = regTrainer.rawPassword();

        // Create trainee
        Trainee trainee = Trainee.builder()
                .user(User.builder().firstName("Alice").lastName("Brown").active(true).build())
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .address("Some address")
                .build();
        var regTrainee = facade.registerTraineeWithPassword(trainee);
        trainee = regTrainee.entity();
        String traineePass = regTrainee.rawPassword();

        // Authentication and profile update
        trainer.setSpecialization(yoga);
        trainer = facade.updateTrainer(trainer.getUser().getUsername(), trainerPass, trainer);
        trainee.setAddress("New address");
        trainee = facade.updateTrainee(trainee.getUser().getUsername(), traineePass, trainee);

        // Change password
        facade.changeTrainerPassword(trainer.getUser().getUsername(), trainerPass, "newTrainerPass");
        facade.changeTraineePassword(trainee.getUser().getUsername(), traineePass, "newTraineePass");

        // Activate/Deactivate
        facade.activateTrainer(trainer.getUser().getUsername(), "newTrainerPass", false);
        facade.activateTrainer(trainer.getUser().getUsername(), "newTrainerPass", true);
        facade.activateTrainee(trainee.getUser().getUsername(), "newTraineePass", false);
        facade.activateTrainee(trainee.getUser().getUsername(), "newTraineePass", true);

        // Create training
        Training training = Training.builder()
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .trainer(trainer)
                .trainee(trainee)
                .trainingType(yoga)
                .build();
        training = facade.scheduleTraining(trainer.getUser().getUsername(), "newTrainerPass", training);

        // Get trainings
        facade.getTraineeTrainings(trainee.getUser().getUsername(), "newTraineePass",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), "Smith", "Yoga");
        facade.getTrainerTrainings(trainer.getUser().getUsername(), "newTrainerPass",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), "Alice");

        // Assign trainers
        facade.updateTraineeTrainers(trainee.getUser().getUsername(), "newTraineePass",
                List.of(trainer.getId()));
        facade.getUnassignedTrainers(trainee.getUser().getUsername(), "newTraineePass");

        // View lists
        facade.listTrainees(trainee.getUser().getUsername(), "newTraineePass");
        facade.listTrainers(trainer.getUser().getUsername(), "newTrainerPass");
        facade.listTrainings(trainer.getUser().getUsername(), "newTrainerPass");

        // Get by username
        facade.getTraineeByUsername(trainee.getUser().getUsername(), "newTraineePass");
        facade.getTrainerByUsername(trainer.getUser().getUsername(), "newTrainerPass");

        // Delete trainee
        Trainee trainee2 = Trainee.builder()
                .user(User.builder().firstName("Bob").lastName("Miller").active(true).build())
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        var regT2 = facade.registerTraineeWithPassword(trainee2);
        facade.deleteTrainee(regT2.entity().getUser().getUsername(), regT2.rawPassword());
    }
}
