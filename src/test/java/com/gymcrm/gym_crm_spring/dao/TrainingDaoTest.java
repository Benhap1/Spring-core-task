package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {

    private TrainingDao dao;
    private Map<UUID, Training> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        dao = new TrainingDao(storage);
    }

    @Test
    void save_assignsIdIfNull_andStoresTraining() {
        Training training = Training.builder()
                .trainerId(UUID.randomUUID())
                .traineeId(UUID.randomUUID())
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(45)
                .build();

        dao.save(training);

        assertNotNull(training.getId(), "ID should be generated if null");
        assertTrue(storage.containsKey(training.getId()));
        assertEquals("Morning Yoga", storage.get(training.getId()).getTrainingName());
    }

    @Test
    void save_preservesProvidedId() {
        UUID id = UUID.randomUUID();
        Training training = Training.builder()
                .id(id)
                .trainerId(UUID.randomUUID())
                .trainingName("Cardio")
                .build();

        dao.save(training);

        assertEquals(id, training.getId(), "Explicit ID should not be overridden");
        assertEquals("Cardio", storage.get(id).getTrainingName());
    }

    @Test
    void findById_returnsTrainingIfExists() {
        UUID id = UUID.randomUUID();
        Training training = Training.builder().id(id).trainingName("Test").build();
        storage.put(id, training);

        Optional<Training> result = dao.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTrainingName());
    }

    @Test
    void findById_returnsEmptyIfNotFound() {
        Optional<Training> result = dao.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllTrainings() {
        Training t1 = Training.builder().id(UUID.randomUUID()).trainingName("Yoga").build();
        Training t2 = Training.builder().id(UUID.randomUUID()).trainingName("Cardio").build();
        storage.put(t1.getId(), t1);
        storage.put(t2.getId(), t2);

        List<Training> all = dao.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(t -> "Yoga".equals(t.getTrainingName())));
    }

    @Test
    void findByTrainerId_returnsOnlyMatching() {
        UUID trainerA = UUID.randomUUID();
        UUID trainerB = UUID.randomUUID();

        Training t1 = Training.builder().id(UUID.randomUUID()).trainerId(trainerA).trainingName("Yoga").build();
        Training t2 = Training.builder().id(UUID.randomUUID()).trainerId(trainerB).trainingName("Cardio").build();
        Training t3 = Training.builder().id(UUID.randomUUID()).trainerId(trainerA).trainingName("Strength").build();

        storage.put(t1.getId(), t1);
        storage.put(t2.getId(), t2);
        storage.put(t3.getId(), t3);

        List<Training> byTrainer = dao.findByTrainerId(trainerA);

        assertEquals(2, byTrainer.size());
        assertTrue(byTrainer.stream().anyMatch(t -> "Yoga".equals(t.getTrainingName())));
        assertTrue(byTrainer.stream().anyMatch(t -> "Strength".equals(t.getTrainingName())));
    }
}
