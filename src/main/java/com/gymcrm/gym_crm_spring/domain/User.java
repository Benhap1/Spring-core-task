package com.gymcrm.gym_crm_spring.domain;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
}