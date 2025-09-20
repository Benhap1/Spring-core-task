package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TrainerDao;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        trainerService = new TrainerService(trainerDao); // ✅ теперь через конструктор

        // save как void
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

        assertNotNull(result.getId()); // UUID
        assertEquals("john.smith", result.getUsername());
        assertNotNull(result.getPassword());
        assertTrue(result.isActive());

        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    void findById_returnsTrainer() {
        UUID id = UUID.randomUUID(); // ✅ UUID вместо строки
        Trainer trainer = Trainer.builder()
                .id(id)
                .firstName("Anna")
                .lastName("Lee")
                .build();

        when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Anna", result.get().getFirstName());
    }

    @Test
    void updateTrainer_callsDaoSave() {
        UUID id = UUID.randomUUID();
        Trainer trainer = Trainer.builder()
                .id(id)
                .firstName("John")
                .lastName("Smith")
                .build();

        when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));

        Trainer updated = trainerService.update(trainer);

        verify(trainerDao).save(trainer);
        assertEquals("John", updated.getFirstName());
    }
}
