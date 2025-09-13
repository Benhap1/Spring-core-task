package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {

    private TrainingDao dao;
    private Map<String, Object> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        dao = new TrainingDao(storage);
    }

    @Test
    void save_assignsIdIfNull_andStoresTraining() {
        Training training = Training.builder()
                .trainerId("t1")
                .traineeId("u1")
                .trainingName("Morning Yoga")
                .trainingDate(LocalDate.now())
                .trainingDurationMinutes(45)
                .build();

        dao.save(training);

        assertNotNull(training.getId(), "ID should be generated if null");
        assertTrue(storage.containsKey(training.getId()));
        assertEquals("Morning Yoga", ((Training) storage.get(training.getId())).getTrainingName());
    }

    @Test
    void save_preservesProvidedId() {
        Training training = Training.builder()
                .id("tr1")
                .trainerId("t1")
                .trainingName("Cardio")
                .build();

        dao.save(training);

        assertEquals("tr1", training.getId(), "Explicit ID should not be overridden");
        assertEquals("Cardio", ((Training) storage.get("tr1")).getTrainingName());
    }

    @Test
    void findById_returnsTrainingIfExists() {
        Training training = Training.builder().id("tr2").trainingName("Test").build();
        storage.put("tr2", training);

        Optional<Training> result = dao.findById("tr2");

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTrainingName());
    }

    @Test
    void findById_returnsEmptyIfNotFound() {
        Optional<Training> result = dao.findById("missing");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllTrainings() {
        storage.put("1", Training.builder().id("1").trainingName("Yoga").build());
        storage.put("2", Training.builder().id("2").trainingName("Cardio").build());

        List<Training> all = dao.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(t -> "Yoga".equals(t.getTrainingName())));
    }

    @Test
    void findByTrainerId_returnsOnlyMatching() {
        Training t1 = Training.builder().id("1").trainerId("A").trainingName("Yoga").build();
        Training t2 = Training.builder().id("2").trainerId("B").trainingName("Cardio").build();
        Training t3 = Training.builder().id("3").trainerId("A").trainingName("Strength").build();

        storage.put(t1.getId(), t1);
        storage.put(t2.getId(), t2);
        storage.put(t3.getId(), t3);

        List<Training> byTrainer = dao.findByTrainerId("A");

        assertEquals(2, byTrainer.size());
        assertTrue(byTrainer.stream().anyMatch(t -> "Yoga".equals(t.getTrainingName())));
        assertTrue(byTrainer.stream().anyMatch(t -> "Strength".equals(t.getTrainingName())));
    }
}
