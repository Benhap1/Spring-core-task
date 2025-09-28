package com.gymcrm.gym_crm_spring;

import com.gymcrm.gym_crm_spring.config.AppConfig;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.domain.TrainingType;
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

        // ⚡ taking TrainingType from database
        TrainingType yoga = trainingTypeService.findByName("Yoga")
                .orElseThrow(() -> new RuntimeException("TrainingType 'Yoga' not found in DB!"));

        // 1. Create Trainer
        Trainer trainer = Trainer.builder()
                .firstName("John")
                .lastName("Smith")
                .username("john.smith")
                .password("secret")
                .active(true)
                .specialization(yoga)
                .build();
        trainer = facade.registerTrainer(trainer);
        System.out.println("New Trainer: " + trainer);

        // 2. Create Trainee
        Trainee trainee = Trainee.builder()
                .firstName("Alice")
                .lastName("Brown")
                .username("alice.brown")
                .password("secret")
                .active(true)
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .address("Some address")
                .build();
        trainee = facade.registerTrainee(trainee);
        System.out.println("New Trainee: " + trainee);

        // 3. Authenticate users
        boolean traineeAuth = facade.authenticateTrainee(trainee.getUsername(), "secret");
        boolean trainerAuth = facade.authenticateTrainer(trainer.getUsername(), "secret");
        System.out.println("Trainee auth = " + traineeAuth);
        System.out.println("Trainer auth = " + trainerAuth);

        // 4. Update profiles
        trainer.setFirstName("Johnny");
        trainer = facade.updateTrainer(trainer);
        System.out.println("Updated Trainer: " + trainer);

        trainee.setAddress("New address");
        trainee = facade.updateTrainee(trainee);
        System.out.println("Updated Trainee: " + trainee);

        // 5. Change passwords
        facade.changeTraineePassword(trainee.getUsername(), "secret", "newTraineePass");
        trainee = facade.getTraineeByUsername(trainee.getUsername());

        facade.changeTrainerPassword(trainer.getUsername(), "secret", "newTrainerPass");
        trainer = facade.getTrainerByUsername(trainer.getUsername());

        // 6. Activate/Deactivate users
        facade.activateTrainee(trainee.getUsername(), false);
        trainee = facade.getTraineeByUsername(trainee.getUsername());
        facade.activateTrainee(trainee.getUsername(), true);
        trainee = facade.getTraineeByUsername(trainee.getUsername());

        facade.activateTrainer(trainer.getUsername(), false);
        trainer = facade.getTrainerByUsername(trainer.getUsername());
        facade.activateTrainer(trainer.getUsername(), true);
        trainer = facade.getTrainerByUsername(trainer.getUsername());

        // 7. Schedule trainings
        Training training = Training.builder()
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(yoga)
                .build();
        training = facade.scheduleTraining(training);
        System.out.println("Created Training: " + training);

        // 8. Fetch trainings with authentication
        LocalDate fromDate = LocalDate.now().minusDays(1);
        LocalDate toDate = LocalDate.now().plusDays(1);
        String trainerLastName = "Smith";
        String trainingType = "Yoga";
        System.out.println("Trainee trainings criteria: from=" + fromDate + ", to=" + toDate + ", trainerName=" + trainerLastName + ", trainingType=" + trainingType);
        List<Training> trainingsForTrainee = facade.getTraineeTrainings(
                trainee.getUsername(), "newTraineePass",
                fromDate, toDate,
                trainerLastName, trainingType
        );
        System.out.println("Trainee trainings by criteria: " + trainingsForTrainee);

        String traineeLastName = "Brown";
        System.out.println("Trainer trainings criteria: from=" + fromDate + ", to=" + toDate + ", traineeName=" + traineeLastName);
        List<Training> trainingsForTrainer = facade.getTrainerTrainings(
                trainer.getUsername(), "newTrainerPass",
                fromDate, toDate,
                traineeLastName
        );
        System.out.println("Trainer trainings by criteria: " + trainingsForTrainer);

        // 9. Delete trainee
        Trainee trainee2 = Trainee.builder()
                .firstName("Bob")
                .lastName("Miller")
                .username("bob.miller")
                .password("12345")
                .active(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        trainee2 = facade.registerTrainee(trainee2);
        facade.deleteTraineeByUsername(trainee2.getUsername());
        System.out.println("Deleted trainee " + trainee2.getUsername());
    }
}
