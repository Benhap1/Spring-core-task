package com.gymcrm.gym_crm_spring.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
}
