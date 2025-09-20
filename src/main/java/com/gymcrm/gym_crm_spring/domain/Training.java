package com.gymcrm.gym_crm_spring.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Training {
    private UUID id;
    private UUID trainerId;
    private UUID traineeId;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private int trainingDurationMinutes;
}
