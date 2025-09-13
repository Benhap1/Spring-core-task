package com.gymcrm.gym_crm_spring.facade;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.service.TraineeService;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import com.gymcrm.gym_crm_spring.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GymFacadeTest {

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private GymFacade facade;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        trainingService = mock(TrainingService.class);
        facade = new GymFacade(traineeService, trainerService, trainingService);
    }

    @Test
    void registerTrainer_delegatesToService() {
        Trainer trainer = Trainer.builder().firstName("John").lastName("Smith").build();
        when(trainerService.listAll()).thenReturn(Collections.emptyList());
        when(traineeService.listAll()).thenReturn(Collections.emptyList());
        when(trainerService.createTrainer(any(), any())).thenReturn(trainer);

        Trainer result = facade.registerTrainer(trainer);

        assertEquals("John", result.getFirstName());
        verify(trainerService).createTrainer(eq(trainer), anyList());
    }

    @Test
    void registerTrainee_delegatesToService() {
        Trainee trainee = Trainee.builder()
                .firstName("Alice")
                .lastName("Brown")
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .build();

        when(trainerService.listAll()).thenReturn(Collections.emptyList());
        when(traineeService.listAll()).thenReturn(Collections.emptyList());
        when(traineeService.createTrainee(any(), any())).thenReturn(trainee);

        Trainee result = facade.registerTrainee(trainee);

        assertEquals("Alice", result.getFirstName());
        verify(traineeService).createTrainee(eq(trainee), anyList());
    }

    @Test
    void scheduleTraining_delegatesToService() {
        Training training = Training.builder()
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        when(trainingService.createTraining(any())).thenReturn(training);

        Training result = facade.scheduleTraining(training);

        assertEquals("Morning Yoga", result.getTrainingName());
        verify(trainingService).createTraining(training);
    }

    @Test
    void listMethods_returnListsFromServices() {
        // given
        Trainer trainer = Trainer.builder()
                .id("t1")
                .firstName("John")
                .lastName("Smith")
                .specialization("Yoga")
                .build();

        Trainee trainee = Trainee.builder()
                .id("u1")
                .firstName("Alice")
                .lastName("Brown")
                .address("London")
                .build();

        Training training = Training.builder()
                .id("tr1")
                .trainerId("t1")
                .traineeId("u1")
                .trainingName("Morning Yoga")
                .trainingType("Yoga")
                .trainingDurationMinutes(60)
                .build();

        when(trainerService.listAll()).thenReturn(List.of(trainer));
        when(traineeService.listAll()).thenReturn(List.of(trainee));
        when(trainingService.listAll()).thenReturn(List.of(training));

        // then
        assertEquals(1, facade.listTrainers().size());
        assertEquals(1, facade.listTrainees().size());
        assertEquals(1, facade.listTrainings().size());

        assertEquals("John", facade.listTrainers().get(0).getFirstName());
        assertEquals("Alice", facade.listTrainees().get(0).getFirstName());
        assertEquals("Morning Yoga", facade.listTrainings().get(0).getTrainingName());
    }

    @Test
    void updateTrainee_delegatesToService() {
        Trainee trainee = Trainee.builder().id("123").firstName("Alice").lastName("Brown").build();
        when(traineeService.update(any())).thenReturn(trainee);

        Trainee result = facade.updateTrainee(trainee);

        verify(traineeService).update(trainee);
        assertEquals("Alice", result.getFirstName());
    }

    @Test
    void deleteTrainee_delegatesToService() {
        doNothing().when(traineeService).delete("123");

        facade.deleteTrainee("123");

        verify(traineeService).delete("123");
    }

    @Test
    void updateTrainer_delegatesToService() {
        Trainer trainer = Trainer.builder().id("t1").firstName("John").lastName("Smith").build();
        when(trainerService.update(any())).thenReturn(trainer);

        Trainer result = facade.updateTrainer(trainer);

        verify(trainerService).update(trainer);
        assertEquals("John", result.getFirstName());
    }

}
