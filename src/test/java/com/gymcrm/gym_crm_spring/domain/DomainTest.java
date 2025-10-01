package com.gymcrm.gym_crm_spring.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DomainTest {

    private static final UUID TRAINER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TRAINEE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID TRAINING_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID TRAINING_TYPE_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Test
    void trainer_builderAndGetters() {
        User user = User.builder()
                .id(TRAINER_ID)
                .firstName("John")
                .lastName("Smith")
                .username("john.smith")
                .password("pass123")
                .active(true)
                .build();

        TrainingType specialization = TrainingType.builder()
                .id(TRAINING_TYPE_ID)
                .trainingTypeName("Yoga")
                .build();

        Trainer trainer = Trainer.builder()
                .id(TRAINER_ID)
                .user(user)
                .specialization(specialization)
                .build();

        assertEquals(TRAINER_ID, trainer.getId());
        assertEquals("John", trainer.getUser().getFirstName());
        assertEquals("Smith", trainer.getUser().getLastName());
        assertEquals("Yoga", trainer.getSpecialization().getTrainingTypeName());
        assertTrue(trainer.getUser().getActive());
    }

    @Test
    void trainee_builderAndGetters() {
        User user = User.builder()
                .id(TRAINEE_ID)
                .firstName("Anna")
                .lastName("Lee")
                .username("anna.lee")
                .password("secret")
                .active(true)
                .build();

        Trainee trainee = Trainee.builder()
                .id(TRAINEE_ID)
                .user(user)
                .build();

        assertEquals(TRAINEE_ID, trainee.getId());
        assertEquals("Anna", trainee.getUser().getFirstName());
        assertEquals("Lee", trainee.getUser().getLastName());
        assertTrue(trainee.getUser().getActive());
    }

    @Test
    void training_builderAndGetters() {
        User trainerUser = User.builder()
                .id(TRAINER_ID)
                .firstName("John")
                .lastName("Smith")
                .username("john.smith")
                .password("pass123")
                .active(true)
                .build();

        User traineeUser = User.builder()
                .id(TRAINEE_ID)
                .firstName("Anna")
                .lastName("Lee")
                .username("anna.lee")
                .password("secret")
                .active(true)
                .build();

        Trainer trainer = Trainer.builder()
                .id(TRAINER_ID)
                .user(trainerUser)
                .build();

        Trainee trainee = Trainee.builder()
                .id(TRAINEE_ID)
                .user(traineeUser)
                .build();

        TrainingType type = TrainingType.builder()
                .id(TRAINING_TYPE_ID)
                .trainingTypeName("Yoga")
                .build();

        LocalDate date = LocalDate.now();

        Training training = Training.builder()
                .id(TRAINING_ID)
                .trainer(trainer)
                .trainee(trainee)
                .trainingType(type)
                .trainingName("Morning Yoga")
                .trainingDate(date)
                .trainingDuration(60)
                .build();

        assertEquals(TRAINING_ID, training.getId());
        assertEquals(TRAINER_ID, training.getTrainer().getId());
        assertEquals(TRAINEE_ID, training.getTrainee().getId());
        assertEquals("Morning Yoga", training.getTrainingName());
        assertEquals("Yoga", training.getTrainingType().getTrainingTypeName());
        assertEquals(date, training.getTrainingDate());
        assertEquals(60, training.getTrainingDuration());
    }
}
