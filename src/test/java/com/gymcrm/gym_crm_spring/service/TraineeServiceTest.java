//package com.gymcrm.gym_crm_spring.service;
//
//import com.gymcrm.gym_crm_spring.dao.TraineeDao;
//import com.gymcrm.gym_crm_spring.domain.Trainee;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.Collections;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceTest {
//
//    private TraineeDao traineeDao;
//    private TraineeService traineeService;
//
//    @BeforeEach
//    void setUp() {
//        traineeDao = mock(TraineeDao.class);
//        traineeService = new TraineeService(traineeDao); // ✅ передаем DAO через конструктор
//
//        doAnswer(invocation -> null).when(traineeDao).save(any(Trainee.class));
//    }
//
//    @Test
//    void createTrainee_assignsIdAndUsernameAndPassword() {
//        Trainee trainee = Trainee.builder()
//                .firstName("Alice")
//                .lastName("Brown")
//                .dateOfBirth(LocalDate.of(1995, 5, 10))
//                .build();
//
//        Trainee result = traineeService.createTrainee(trainee, Collections.emptyList());
//
//        assertNotNull(result.getId());
//        assertEquals("alice.brown", result.getUsername());
//        assertNotNull(result.getPassword());
//        assertTrue(result.isActive());
//
//        verify(traineeDao).save(any(Trainee.class));
//    }
//
//    @Test
//    void findById_returnsTrainee() {
//        UUID id = UUID.randomUUID();
//        Trainee trainee = Trainee.builder().id(id).firstName("Bob").lastName("Miller").build();
//        when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> result = traineeService.findById(id);
//
//        assertTrue(result.isPresent());
//        assertEquals("Bob", result.get().getFirstName());
//    }
//
//    @Test
//    void updateTrainee_callsDaoSave() {
//        UUID id = UUID.randomUUID();
//        Trainee trainee = Trainee.builder().id(id).firstName("Alice").lastName("Brown").build();
//        when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));
//
//        Trainee updated = traineeService.update(trainee);
//
//        verify(traineeDao).save(trainee);
//        assertEquals("Alice", updated.getFirstName());
//    }
//
//    @Test
//    void deleteTrainee_callsDaoDelete() {
//        UUID id = UUID.randomUUID();
//
//        traineeService.delete(id);
//
//        verify(traineeDao).delete(id);
//    }
//}
