//package com.gymcrm.gym_crm_spring.domain;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DomainTest {
//
//    private static final UUID TRAINER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
//    private static final UUID TRAINEE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
//    private static final UUID TRAINING_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
//
//    @Test
//    void trainer_builderAndGetters() {
//        Trainer trainer = Trainer.builder()
//                .id(TRAINER_ID)
//                .firstName("John")
//                .lastName("Smith")
//                .specialization("Yoga")
//                .username("john.smith")
//                .password("pass123")
//                .active(true)
//                .build();
//
//        assertEquals(TRAINER_ID, trainer.getId());
//        assertEquals("John", trainer.getFirstName());
//        assertEquals("Smith", trainer.getLastName());
//        assertEquals("Yoga", trainer.getSpecialization());
//        assertTrue(trainer.isActive());
//    }
//
//    @Test
//    void trainee_builderAndGetters() {
//        LocalDate dob = LocalDate.of(1990, 5, 20);
//        Trainee trainee = Trainee.builder()
//                .id(TRAINEE_ID)
//                .firstName("Anna")
//                .lastName("Lee")
//                .dateOfBirth(dob)
//                .address("123 Street")
//                .username("anna.lee")
//                .password("secret")
//                .active(true)
//                .build();
//
//        assertEquals(TRAINEE_ID, trainee.getId());
//        assertEquals("Anna", trainee.getFirstName());
//        assertEquals("Lee", trainee.getLastName());
//        assertEquals(dob, trainee.getDateOfBirth());
//        assertEquals("123 Street", trainee.getAddress());
//        assertTrue(trainee.isActive());
//    }
//
//    @Test
//    void training_builderAndGetters() {
//        LocalDate date = LocalDate.now();
//        Training training = Training.builder()
//                .id(TRAINING_ID)
//                .trainerId(TRAINER_ID)
//                .traineeId(TRAINEE_ID)
//                .trainingName("Morning Yoga")
//                .trainingType("Yoga")
//                .trainingDate(date)
//                .trainingDurationMinutes(60)
//                .build();
//
//        assertEquals(TRAINING_ID, training.getId());
//        assertEquals(TRAINER_ID, training.getTrainerId());
//        assertEquals(TRAINEE_ID, training.getTraineeId());
//        assertEquals("Morning Yoga", training.getTrainingName());
//        assertEquals("Yoga", training.getTrainingType());
//        assertEquals(date, training.getTrainingDate());
//        assertEquals(60, training.getTrainingDurationMinutes());
//    }
//}
