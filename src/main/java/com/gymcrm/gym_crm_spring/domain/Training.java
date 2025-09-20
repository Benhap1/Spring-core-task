package com.gymcrm.gym_crm_spring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
