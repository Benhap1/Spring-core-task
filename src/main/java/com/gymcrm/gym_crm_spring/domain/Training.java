package com.gymcrm.gym_crm_spring.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Training {
    private String id;
    private String trainerId;
    private String traineeId;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private int trainingDurationMinutes;

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", traineeId='" + traineeId + '\'' +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType='" + trainingType + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDurationMinutes=" + trainingDurationMinutes +
                '}';
    }
}
