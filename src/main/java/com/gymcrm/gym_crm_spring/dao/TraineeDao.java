package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Trainee;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TraineeDao extends AbstractDaoJpa<Trainee> {

    public Optional<Trainee> findByUsername(String username) {
        TypedQuery<Trainee> q = getEntityManager()
                .createQuery("select t from Trainee t where lower(t.user.username) = :u", Trainee.class)
                .setParameter("u", username.toLowerCase());
        return q.getResultStream().findFirst();
    }
}
