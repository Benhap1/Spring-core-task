package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainingDao;
import com.gymcrm.gym_crm_spring.domain.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingDao trainingDao;
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDao.class);
        trainingService = new TrainingService(trainingDao);

        doAnswer(invocation -> null).when(trainingDao).save(any(Training.class));
    }

    @Test
    void createTraining_savesTraining() {
        Training training = Training.builder()
                .trainerId("t1")
                .traineeId("u1")
                .trainingName("Morning Yoga")
                .trainingType("Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(60)
                .build();

        Training result = trainingService.createTraining(training);

        assertEquals("Morning Yoga", result.getTrainingName());
        assertNotNull(result.getId());

        // Checking via ArgumentCaptor
        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(trainingDao).save(captor.capture());

        Training saved = captor.getValue();
        assertEquals("Morning Yoga", saved.getTrainingName());
        assertEquals("t1", saved.getTrainerId());
        assertEquals("u1", saved.getTraineeId());
    }

    @Test
    void listAll_returnsTrainings() {
        Training tr1 = Training.builder().id("1").trainingName("A").build();
        Training tr2 = Training.builder().id("2").trainingName("B").build();

        when(trainingDao.findAll()).thenReturn(Arrays.asList(tr1, tr2));

        List<Training> trainings = trainingService.listAll();

        assertEquals(2, trainings.size());
        assertEquals("A", trainings.get(0).getTrainingName());
    }
}
