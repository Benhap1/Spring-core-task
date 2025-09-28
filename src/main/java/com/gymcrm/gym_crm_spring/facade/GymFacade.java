package com.gymcrm.gym_crm_spring.facade;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.Training;
import com.gymcrm.gym_crm_spring.service.TraineeService;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import com.gymcrm.gym_crm_spring.service.TrainingService;
import com.gymcrm.gym_crm_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    // 1. Create Trainee
    @Transactional
    public Trainee registerTrainee(Trainee trainee) {
        trainee.setActive(true);
        return traineeService.save(trainee);
    }

    // 2. Create Trainer
    @Transactional
    public Trainer registerTrainer(Trainer trainer) {
        trainer.setActive(true);
        return trainerService.save(trainer);
    }

    // 3. Trainee username/password matching
    @Transactional(readOnly = true)
    public boolean authenticateTrainee(String username, String password) {
        return userService.findByUsername(username)
                .filter(u -> u instanceof Trainee)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    // 4. Trainer username/password matching
    @Transactional(readOnly = true)
    public boolean authenticateTrainer(String username, String password) {
        return userService.findByUsername(username)
                .filter(u -> u instanceof Trainer)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    // 5. Select Trainer by username
    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username) {
        return (Trainer) userService.findByUsername(username)
                .filter(u -> u instanceof Trainer)
                .orElseThrow();
    }

    // 6. Select Trainee by username
    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        return (Trainee) userService.findByUsername(username)
                .filter(u -> u instanceof Trainee)
                .orElseThrow();
    }

    // 7. Trainee password change
    @Transactional
    public void changeTraineePassword(String username, String oldPass, String newPass) {
        Trainee t = getTraineeByUsername(username);
        if (!t.getPassword().equals(oldPass)) throw new IllegalArgumentException("Wrong password");
        t.setPassword(newPass);
        traineeService.save(t);
    }

    // 8. Trainer password change
    @Transactional
    public void changeTrainerPassword(String username, String oldPass, String newPass) {
        Trainer tr = getTrainerByUsername(username);
        if (!tr.getPassword().equals(oldPass)) throw new IllegalArgumentException("Wrong password");
        tr.setPassword(newPass);
        trainerService.save(tr);
    }

    // 9. Update trainer profile
    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.save(trainer);
    }

    // 10. Update trainee profile
    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.save(trainee);
    }

    // 11. Activate/Deactivate trainee
    public void activateTrainee(String username, boolean active) {
        Trainee t = getTraineeByUsername(username);
        t.setActive(active);
        traineeService.save(t);
    }

    // 12. Activate/Deactivate trainer
    @Transactional
    public void activateTrainer(String username, boolean active) {
        Trainer tr = getTrainerByUsername(username);
        tr.setActive(active);
        trainerService.save(tr);
    }

    // 13. Delete trainee by username
    @Transactional
    public void deleteTraineeByUsername(String username) {
        Trainee t = getTraineeByUsername(username);
        traineeService.delete(t.getId()); // каскадно удалит тренировки
    }

    // 14. Get Trainee Trainings by criteria
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, String password,
                                              LocalDate from, LocalDate to,
                                              String trainerName, String trainingType) {
        if (!authenticateTrainee(username, password)) throw new SecurityException("Auth failed");
        Trainee t = getTraineeByUsername(username);
        return trainingService.getByCondition(tr ->
                tr.getTrainee().equals(t) &&
                        (from == null || !tr.getTrainingDate().isBefore(from)) &&
                        (to == null || !tr.getTrainingDate().isAfter(to)) &&
                        (trainerName == null || tr.getTrainer().getLastName().equalsIgnoreCase(trainerName)) &&
                        (trainingType == null || tr.getTrainingType().getTrainingTypeName().equalsIgnoreCase(trainingType))
        );
    }

    // 15. Get Trainer Trainings by criteria
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, String password,
                                              LocalDate from, LocalDate to,
                                              String traineeName) {
        if (!authenticateTrainer(username, password)) throw new SecurityException("Auth failed");
        Trainer tr = getTrainerByUsername(username);
        return trainingService.getByCondition(t ->
                t.getTrainer().equals(tr) &&
                        (from == null || !t.getTrainingDate().isBefore(from)) &&
                        (to == null || !t.getTrainingDate().isAfter(to)) &&
                        (traineeName == null || t.getTrainee().getLastName().equalsIgnoreCase(traineeName))
        );
    }

    // 16. Add training
    @Transactional
    public Training scheduleTraining(Training training) {
        return trainingService.save(training);
    }

    // 17. Get trainers not assigned to trainee
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        if (!authenticateTrainee(traineeUsername, password)) throw new SecurityException("Auth failed");
        Trainee trainee = getTraineeByUsername(traineeUsername);
        List<Trainer> all = trainerService.findAll();
        all.removeAll(
                trainingService.getByCondition(tr -> tr.getTrainee().equals(trainee))
                        .stream()
                        .map(Training::getTrainer)
                        .toList()
        );
        return all;
    }

    // 18. Update Trainee's trainers list
    @Transactional
    public void updateTraineeTrainers(String traineeUsername, String password, List<UUID> trainerIds) {
        if (!authenticateTrainee(traineeUsername, password)) throw new SecurityException("Auth failed");
        Trainee trainee = getTraineeByUsername(traineeUsername);
        // Удаляем старые тренировки
        trainingService.getByCondition(tr -> tr.getTrainee().equals(trainee))
                .forEach(t -> trainingService.delete(t.getId()));
        // Создаем новые "привязки"
        trainerIds.forEach(id -> {
            Trainer tr = trainerService.findById(id).orElseThrow();
            Training link = Training.builder()
                    .trainee(trainee)
                    .trainer(tr)
                    .trainingName("Assignment")
                    .trainingDate(LocalDate.now())
                    .trainingDuration(0)
                    .build();
            trainingService.save(link);
        });
    }

    // ---- LIST ----
    @Transactional(readOnly = true)
    public List<Trainee> listTrainees() {
        return traineeService.findAll();
    }

    @Transactional(readOnly = true)
    public List<Trainer> listTrainers() {
        return trainerService.findAll();
    }

    @Transactional(readOnly = true)
    public List<Training> listTrainings() {
        return trainingService.findAll();
    }
}
