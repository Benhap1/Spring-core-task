package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.TrainingType;
import com.gymcrm.gym_crm_spring.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private TrainerService trainerService;
    private TrainingType yoga;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        trainerService = new TrainerService(trainerDao);

        yoga = TrainingType.builder()
                .id(UUID.randomUUID())
                .trainingTypeName("Yoga")
                .build();

        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void saveTrainer_assignsIdAndProperties() {
        Trainer trainer = Trainer.builder()
                .user(User.builder().firstName("John").lastName("Smith").active(true).build())
                .specialization(yoga)
                .build();

        Trainer saved = trainerService.save(trainer);

        assertNotNull(saved);
        assertEquals("John", saved.getUser().getFirstName());
        assertEquals("Smith", saved.getUser().getLastName());

        verify(trainerDao).save(trainer);
    }

    @Test
    void findById_returnsTrainer() {
        UUID id = UUID.randomUUID();
        Trainer trainer = Trainer.builder()
                .id(id)
                .user(User.builder().firstName("Anna").lastName("Lee").active(true).build())
                .specialization(yoga)
                .build();

        when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Anna", result.get().getUser().getFirstName());
    }
}
