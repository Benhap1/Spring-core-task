package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class TrainingDaoTest {

    private TrainingDao dao;
    private EntityManager em;
    private TypedQuery<Training> typedQuery;

    @BeforeEach
    void setUp() {
        em = mock(EntityManager.class);
        typedQuery = mock(TypedQuery.class);

        dao = new TrainingDao();

        try {
            var field = AbstractDaoJpa.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(dao, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save_callsMerge() {
        Training training = Training.builder().build();
        when(em.merge(training)).thenReturn(training);

        Training result = dao.save(training);

        verify(em).merge(training);
        assertEquals(training, result);
    }

    @Test
    void findById_callsFind() {
        UUID id = UUID.randomUUID();
        Training training = Training.builder().build();
        when(em.find(Training.class, id)).thenReturn(training);

        Optional<Training> result = dao.findById(id);

        verify(em).find(Training.class, id);
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void findAll_callsCreateQuery() {
        List<Training> list = new ArrayList<>();
        when(em.createQuery("select e from Training e", Training.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);

        List<Training> result = dao.findAll();

        verify(em).createQuery("select e from Training e", Training.class);
        verify(typedQuery).getResultList();
        assertEquals(list, result);
    }

    @Test
    void delete_callsRemove() {
        UUID id = UUID.randomUUID();
        Training training = Training.builder().build();
        when(em.find(Training.class, id)).thenReturn(training);
        when(em.contains(training)).thenReturn(true);

        dao.delete(id);

        verify(em).remove(training);
    }

    @Test
    void findByCriteriaForTrainee_callsQuery() {
        when(em.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        List<Training> result = dao.findByCriteriaForTrainee("user", null, null, null, null);

        verify(em).createQuery(anyString(), eq(Training.class));
        verify(typedQuery).setParameter("trainee", "user");
        verify(typedQuery).getResultList();
        assertNotNull(result);
    }

    @Test
    void findByCriteriaForTrainer_callsQuery() {
        when(em.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        List<Training> result = dao.findByCriteriaForTrainer("trainer", null, null, null);

        verify(em).createQuery(anyString(), eq(Training.class));
        verify(typedQuery).setParameter("trainer", "trainer");
        verify(typedQuery).getResultList();
        assertNotNull(result);
    }
}
