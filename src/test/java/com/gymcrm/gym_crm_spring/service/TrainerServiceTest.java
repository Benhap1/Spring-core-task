package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        trainerService = new TrainerService();
        trainerService.setTrainerDao(trainerDao);

        // save as void
        doAnswer(invocation -> null).when(trainerDao).save(any(Trainer.class));
    }

    @Test
    void createTrainer_assignsIdAndUsernameAndPassword() {
        Trainer trainer = Trainer.builder()
                .firstName("John")
                .lastName("Smith")
                .specialization("Yoga")
                .build();

        Trainer result = trainerService.createTrainer(trainer, Collections.emptyList());

        assertNotNull(result.getId());
        assertEquals("John.Smith", result.getUsername());
        assertNotNull(result.getPassword());
        assertTrue(result.isActive());

        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    void findById_returnsTrainer() {
        Trainer trainer = Trainer.builder().id("123").firstName("Anna").lastName("Lee").build();
        when(trainerDao.findById("123")).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById("123");

        assertTrue(result.isPresent());
        assertEquals("Anna", result.get().getFirstName());
    }

    @Test
    void updateTrainer_callsDaoSave() {
        Trainer trainer = Trainer.builder().id("t1").firstName("John").lastName("Smith").build();
        when(trainerDao.findById("t1")).thenReturn(Optional.of(trainer));

        Trainer updated = trainerService.update(trainer);

        verify(trainerDao).save(trainer);
        assertEquals("John", updated.getFirstName());
    }

}
