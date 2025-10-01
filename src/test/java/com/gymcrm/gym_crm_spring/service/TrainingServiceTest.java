package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainingDao;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.domain.TrainingType;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    private TrainingDao trainingDao;
    private TrainingService trainingService;
    private TrainingType yoga;
    private Trainer trainer;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDao.class);
        trainingService = new TrainingService(trainingDao);

        yoga = TrainingType.builder()
                .id(UUID.randomUUID())
                .trainingTypeName("Yoga")
                .build();

        trainer = Trainer.builder()
                .id(UUID.randomUUID())
                .user(User.builder().firstName("John").lastName("Smith").active(true).build())
                .specialization(yoga)
                .build();

        trainee = Trainee.builder()
                .id(UUID.randomUUID())
                .user(User.builder().firstName("Alice").lastName("Brown").active(true).build())
                .build();

        when(trainingDao.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void saveTraining_savesTraining() {
        Training training = Training.builder()
                .trainer(trainer)
                .trainee(trainee)
                .trainingName("Morning Yoga")
                .trainingType(yoga)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .build();

        Training saved = trainingService.save(training);

        assertNotNull(saved);
        assertEquals("Morning Yoga", saved.getTrainingName());

        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(trainingDao).save(captor.capture());

        Training captured = captor.getValue();
        assertEquals("Morning Yoga", captured.getTrainingName());
        assertEquals(trainer.getId(), captured.getTrainer().getId());
        assertEquals(trainee.getId(), captured.getTrainee().getId());
    }

    @Test
    void findAll_returnsTrainings() {
        Training tr1 = Training.builder().id(UUID.randomUUID()).trainingName("A").build();
        Training tr2 = Training.builder().id(UUID.randomUUID()).trainingName("B").build();

        when(trainingDao.findAll()).thenReturn(List.of(tr1, tr2));

        List<Training> trainings = trainingService.findAll();

        assertEquals(2, trainings.size());
        assertEquals("A", trainings.get(0).getTrainingName());
    }
}
