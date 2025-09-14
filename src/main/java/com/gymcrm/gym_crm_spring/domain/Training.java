package com.gymcrm.gym_crm_spring.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Training {
    private String id;
    private String trainerId;
    private String traineeId;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private int trainingDurationMinutes;
}
