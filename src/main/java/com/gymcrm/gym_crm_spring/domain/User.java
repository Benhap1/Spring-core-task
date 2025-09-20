package com.gymcrm.gym_crm_spring.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode
public abstract class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
}