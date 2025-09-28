package com.gymcrm.gym_crm_spring.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "trainer")
public class Trainer extends User {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false)
    private TrainingType specialization;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    @Builder.Default
    private Set<Trainee> assignedTrainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Training> trainings = new HashSet<>();
}
