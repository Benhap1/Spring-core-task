package com.gymcrm.gym_crm_spring.dao;

import com.gymcrm.gym_crm_spring.domain.Training;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


@Repository
public class TrainingDao extends AbstractDaoJpa<Training> {

    public List<Training> findByCriteriaForTrainee(String traineeUsername, LocalDate from, LocalDate to, String trainerName, String trainingType) {
        StringBuilder jpql = new StringBuilder("select tr from Training tr where lower(tr.trainee.user.username)=:trainee");
        if (from != null) jpql.append(" and tr.trainingDate >= :from");
        if (to != null) jpql.append(" and tr.trainingDate <= :to");
        if (trainerName != null) jpql.append(" and (lower(tr.trainer.user.firstName) like :tname or lower(tr.trainer.user.lastName) like :tname)");
        if (trainingType != null) jpql.append(" and lower(tr.trainingType.trainingTypeName) = :ttype");

        TypedQuery<Training> q = getEntityManager().createQuery(jpql.toString(), Training.class);
        q.setParameter("trainee", traineeUsername.toLowerCase());
        if (from != null) q.setParameter("from", from);
        if (to != null) q.setParameter("to", to);
        if (trainerName != null) q.setParameter("tname", "%" + trainerName.toLowerCase() + "%");
        if (trainingType != null) q.setParameter("ttype", trainingType.toLowerCase());
        return q.getResultList();
    }

    public List<Training> findByCriteriaForTrainer(String trainerUsername, LocalDate from, LocalDate to, String traineeName) {
        StringBuilder jpql = new StringBuilder("select tr from Training tr where lower(tr.trainer.user.username)=:trainer");
        if (from != null) jpql.append(" and tr.trainingDate >= :from");
        if (to != null) jpql.append(" and tr.trainingDate <= :to");
        if (traineeName != null) jpql.append(" and (lower(tr.trainee.user.firstName) like :tname or lower(tr.trainee.user.lastName) like :tname)");

        TypedQuery<Training> q = getEntityManager().createQuery(jpql.toString(), Training.class);
        q.setParameter("trainer", trainerUsername.toLowerCase());
        if (from != null) q.setParameter("from", from);
        if (to != null) q.setParameter("to", to);
        if (traineeName != null) q.setParameter("tname", "%" + traineeName.toLowerCase() + "%");
        return q.getResultList();
    }
}
