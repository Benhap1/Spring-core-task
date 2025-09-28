package com.gymcrm.gym_crm_spring.facade;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.domain.User;
import com.gymcrm.gym_crm_spring.service.TraineeService;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import com.gymcrm.gym_crm_spring.service.TrainingService;
import com.gymcrm.gym_crm_spring.service.UserService;
import com.gymcrm.gym_crm_spring.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    // --- Registration with raw password ---
    public record RegistrationResult<T>(T entity, String rawPassword) {}

    @Transactional
    public RegistrationResult<Trainee> registerTraineeWithPassword(Trainee trainee) {
        User user = trainee.getUser();
        user.setActive(true);

        String username = UserUtils.generateUsername(user.getFirstName(), user.getLastName(), userService.findAll());
        String rawPassword = UserUtils.generatePassword();
        user.setUsername(username);
        user.setPassword(UserUtils.encodePassword(rawPassword));

        Trainee saved = traineeService.save(trainee);
        log.info("Created trainee {} username={} rawPassword={}", user.getFirstName(), username, rawPassword);
        return new RegistrationResult<>(saved, rawPassword);
    }

    @Transactional
    public RegistrationResult<Trainer> registerTrainerWithPassword(Trainer trainer) {
        User user = trainer.getUser();
        user.setActive(true);

        String username = UserUtils.generateUsername(user.getFirstName(), user.getLastName(), userService.findAll());
        String rawPassword = UserUtils.generatePassword();
        user.setUsername(username);
        user.setPassword(UserUtils.encodePassword(rawPassword));

        Trainer saved = trainerService.save(trainer);
        log.info("Created trainer {} username={} rawPassword={}", user.getFirstName(), username, rawPassword);
        return new RegistrationResult<>(saved, rawPassword);
    }

    // --- Authentication ---
    private User authenticate(String username, String password, boolean mustBeTrainer) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Authentication failed for user: " + username));

        if (!encoder.matches(password, user.getPassword())) {
            throw new SecurityException("Authentication failed for user: " + username);
        }

        if (mustBeTrainer) {
            trainerService.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User is not a trainer: " + username));
        } else {
            traineeService.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User is not a trainee: " + username));
        }

        return user;
    }

    // --- Password change ---
    @Transactional
    public void changeTraineePassword(String username, String oldPass, String newPass) {
        Trainee trainee = traineeService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainee not found: " + username));
        authenticate(username, oldPass, false);

        trainee.getUser().setPassword(UserUtils.encodePassword(newPass));
        userService.save(trainee.getUser());
    }

    @Transactional
    public void changeTrainerPassword(String username, String oldPass, String newPass) {
        Trainer trainer = trainerService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainer not found: " + username));
        authenticate(username, oldPass, true);

        trainer.getUser().setPassword(UserUtils.encodePassword(newPass));
        userService.save(trainer.getUser());
    }

    // --- Update profile ---
    @Transactional
    public Trainer updateTrainer(String username, String password, Trainer updated) {
        authenticate(username, password, true);
        return trainerService.save(updated);
    }

    @Transactional
    public Trainee updateTrainee(String username, String password, Trainee updated) {
        authenticate(username, password, false);
        return traineeService.save(updated);
    }

    // --- Activate / Deactivate ---
    @Transactional
    public void activateTrainer(String username, String password, boolean active) {
        Trainer trainer = trainerService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainer not found: " + username));
        authenticate(username, password, true);

        trainer.getUser().setActive(active);
        userService.save(trainer.getUser());
    }

    @Transactional
    public void activateTrainee(String username, String password, boolean active) {
        Trainee trainee = traineeService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainee not found: " + username));
        authenticate(username, password, false);

        trainee.getUser().setActive(active);
        userService.save(trainee.getUser());
    }

    // --- Delete Trainee ---
    @Transactional
    public void deleteTrainee(String username, String password) {
        authenticate(username, password, false);
        Trainee trainee = traineeService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + username));
        traineeService.delete(trainee.getId());
    }

    // --- Trainings ---
    @Transactional
    public Training scheduleTraining(String username, String password, Training training) {
        Trainer trainer = trainerService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainer not found: " + username));
        authenticate(username, password, true);

        training.setTrainer(trainer);
        return trainingService.save(training);
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, String password,
                                              LocalDate from, LocalDate to,
                                              String trainerName, String trainingType) {
        Trainee trainee = traineeService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainee not found: " + username));
        authenticate(username, password, false);

        return trainingService.findByCriteriaForTrainee(
                trainee.getUser().getUsername(), from, to, trainerName, trainingType
        );
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, String password,
                                              LocalDate from, LocalDate to,
                                              String traineeName) {
        Trainer trainer = trainerService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Trainer not found: " + username));
        authenticate(username, password, true);

        return trainingService.findByCriteriaForTrainer(
                trainer.getUser().getUsername(), from, to, traineeName
        );
    }

    // --- Assign Trainers ---
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        Trainee trainee = traineeService.findByUsername(traineeUsername)
                .orElseThrow(() -> new SecurityException("Trainee not found: " + traineeUsername));
        authenticate(traineeUsername, password, false);

        return trainerService.findNotAssignedToTrainee(trainee.getId());
    }

    @Transactional
    public void updateTraineeTrainers(String traineeUsername, String password, List<UUID> trainerIds) {
        Trainee trainee = traineeService.findByUsername(traineeUsername)
                .orElseThrow(() -> new SecurityException("Trainee not found: " + traineeUsername));
        authenticate(traineeUsername, password, false);

        Set<Trainer> newTrainers = trainerIds.stream()
                .map(id -> trainerService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + id)))
                .collect(Collectors.toSet());

        trainee.setAssignedTrainers(newTrainers);
        traineeService.save(trainee);
    }

    @Transactional(readOnly = true)
    public List<Trainee> listTrainees(String username, String password) {
        userService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("User not found: " + username));
        return traineeService.findAll();
    }

    @Transactional(readOnly = true)
    public List<Trainer> listTrainers(String username, String password) {
        userService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("User not found: " + username));
        return trainerService.findAll();
    }

    @Transactional(readOnly = true)
    public List<Training> listTrainings(String username, String password) {
        userService.findByUsername(username)
                .orElseThrow(() -> new SecurityException("User not found: " + username));
        return trainingService.findAll();
    }


    // --- Get by Username ---
    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username, String password) {
        authenticate(username, password, true);
        return trainerService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + username));
    }

    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username, String password) {
        authenticate(username, password, false);
        return traineeService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + username));
    }
}
