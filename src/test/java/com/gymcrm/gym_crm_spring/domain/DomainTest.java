package com.gymcrm.gym_crm_spring.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DomainTest {

    @Test
    void trainerBuilder_setsAllFieldsCorrectly() {
        Trainer trainer = Trainer.builder()
                .id("t1")
                .firstName("John")
                .lastName("Smith")
                .specialization("Yoga")
                .active(true)
                .build();

        assertEquals("t1", trainer.getId());
        assertEquals("John", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("Yoga", trainer.getSpecialization());
        assertTrue(trainer.isActive());
        assertTrue(trainer.toString().contains("Yoga"));
    }

    @Test
    void traineeBuilder_setsAllFieldsCorrectly() {
        Trainee trainee = Trainee.builder()
                .id("u1")
                .firstName("Alice")
                .lastName("Brown")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .active(false)
                .build();

        assertEquals("u1", trainee.getId());
        assertEquals("Alice", trainee.getFirstName());
        assertEquals("Brown", trainee.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), trainee.getDateOfBirth());
        assertFalse(trainee.isActive());
        assertTrue(trainee.toString().contains("Alice"));
    }

    @Test
    void trainingBuilder_setsAllFieldsCorrectly() {
        Training training = Training.builder()
                .id("tr1")
                .trainerId("t1")
                .traineeId("u1")
                .trainingName("Cardio")
                .trainingType("Fitness")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        assertEquals("tr1", training.getId());
        assertEquals("t1", training.getTrainerId());
        assertEquals("u1", training.getTraineeId());
        assertEquals("Cardio", training.getTrainingName());
        assertEquals("Fitness", training.getTrainingType());
        assertEquals(60, training.getTrainingDurationMinutes());
        assertTrue(training.toString().contains("Cardio"));
    }
}
