package com.gymcrm.gym_crm_spring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User {
    private String specialization;

    @Override
    public String toString() {
        return "Trainer{" +
                "id='" + getId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
