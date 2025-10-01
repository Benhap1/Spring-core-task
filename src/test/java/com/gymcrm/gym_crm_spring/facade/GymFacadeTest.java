package com.gymcrm.gym_crm_spring.facade;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.service.TraineeService;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import com.gymcrm.gym_crm_spring.service.TrainingService;
import com.gymcrm.gym_crm_spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;



class GymFacadeTest {

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    private GymFacade facade;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        trainingService = mock(TrainingService.class);
        userService = mock(UserService.class);
        encoder = new BCryptPasswordEncoder();

        facade = new GymFacade(traineeService, trainerService, trainingService, userService, encoder);
    }

    @Test
    void registerTraineeWithPassword_shouldReturnSavedTraineeAndRawPassword() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Smith");
        trainee.setUser(user);

        when(userService.findAll()).thenReturn(List.of());
        when(traineeService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GymFacade.RegistrationResult<Trainee> result = facade.registerTraineeWithPassword(trainee);

        assertThat(result.entity().getUser().getUsername()).isNotNull();
        assertThat(result.rawPassword()).isNotNull();
        assertThat(encoder.matches(result.rawPassword(), result.entity().getUser().getPassword())).isTrue();

        verify(traineeService, times(1)).save(any());
    }

    @Test
    void registerTrainerWithPassword_shouldReturnSavedTrainerAndRawPassword() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        trainer.setUser(user);

        when(userService.findAll()).thenReturn(List.of());
        when(trainerService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GymFacade.RegistrationResult<Trainer> result = facade.registerTrainerWithPassword(trainer);

        assertThat(result.entity().getUser().getUsername()).isNotNull();
        assertThat(result.rawPassword()).isNotNull();
        assertThat(encoder.matches(result.rawPassword(), result.entity().getUser().getPassword())).isTrue();

        verify(trainerService, times(1)).save(any());
    }

    @Test
    void changeTraineePassword_shouldUpdatePassword() {
        String username = "alice";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(oldPassword));
        trainee.setUser(user);

        when(traineeService.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        facade.changeTraineePassword(username, oldPassword, newPassword);

        assertThat(encoder.matches(newPassword, trainee.getUser().getPassword())).isTrue();
        verify(userService, times(1)).save(trainee.getUser());
    }

    @Test
    void changeTrainerPassword_shouldUpdatePassword() {
        String username = "john";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(oldPassword));
        trainer.setUser(user);

        when(trainerService.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        facade.changeTrainerPassword(username, oldPassword, newPassword);

        assertThat(encoder.matches(newPassword, trainer.getUser().getPassword())).isTrue();
        verify(userService, times(1)).save(trainer.getUser());
    }

    @Test
    void changeTrainerPassword_shouldThrowWhenWrongOldPassword() {
        String username = "john";
        String oldPassword = "oldPass";
        String wrongPassword = "wrong";
        String newPassword = "newPass";

        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(oldPassword));
        trainer.setUser(user);

        when(trainerService.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(SecurityException.class,
                () -> facade.changeTrainerPassword(username, wrongPassword, newPassword));
    }

    @Test
    void getTraineeByUsername_shouldReturnTrainee() {
        String username = "alice";
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode("pass"));

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeService.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        Trainee result = facade.getTraineeByUsername(username, "pass");

        assertThat(result).isEqualTo(trainee);
    }

    @Test
    void getTrainerByUsername_shouldReturnTrainer() {
        String username = "john";
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode("pass"));

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerService.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        Trainer result = facade.getTrainerByUsername(username, "pass");

        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void getTraineeByUsername_shouldThrowWhenNotFound() {
        String username = "missing";
        when(traineeService.findByUsername(username)).thenReturn(Optional.empty());
        when(userService.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(SecurityException.class,
                () -> facade.getTraineeByUsername(username, "anyPass"));
    }

    @Test
    void getTrainerByUsername_shouldThrowWhenNotTrainer() {
        String username = "john";
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode("pass"));

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(trainerService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(SecurityException.class,
                () -> facade.getTrainerByUsername(username, "pass"));
    }
}
