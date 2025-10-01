package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.TraineeDao;
import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {

    private TraineeDao traineeDao;
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDao.class);
        traineeService = new TraineeService(traineeDao);
    }

    @Test
    void save_callsDaoSave() {
        Trainee trainee = Trainee.builder()
                .user(User.builder().firstName("Alice").lastName("Brown").active(true).build())
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .build();

        when(traineeDao.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.save(trainee);

        assertNotNull(result);
        assertEquals("Alice", result.getUser().getFirstName());

        verify(traineeDao).save(trainee);
    }

    @Test
    void findById_returnsTrainee() {
        UUID id = UUID.randomUUID();
        Trainee trainee = Trainee.builder()
                .user(User.builder().firstName("Bob").lastName("Miller").active(true).build())
                .build();
        when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getUser().getFirstName());
        verify(traineeDao).findById(id);
    }

    @Test
    void findAll_callsDaoFindAll() {
        Trainee trainee = Trainee.builder()
                .user(User.builder().firstName("Charlie").lastName("Smith").active(true).build())
                .build();
        when(traineeDao.findAll()).thenReturn(java.util.List.of(trainee));

        var result = traineeService.findAll();

        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getUser().getFirstName());
        verify(traineeDao).findAll();
    }

    @Test
    void delete_callsDaoDelete() {
        UUID id = UUID.randomUUID();

        traineeService.delete(id);

        verify(traineeDao).delete(id);
    }

    @Test
    void findByUsername_callsDaoFindByUsername() {
        String username = "alice.brown";
        Trainee trainee = Trainee.builder()
                .user(User.builder().firstName("Alice").lastName("Brown").active(true).build())
                .build();
        when(traineeDao.findByUsername(username)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getUser().getFirstName());
        verify(traineeDao).findByUsername(username);
    }
}
