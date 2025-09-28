package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.AbstractDaoJpa;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Transactional
public abstract class AbstractService<T> {

    protected final AbstractDaoJpa<T> dao;

    protected AbstractService(AbstractDaoJpa<T> dao) {
        this.dao = dao;
    }


    public T save(T e) {
        return dao.save(e);
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(UUID id) {
        return dao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return dao.findAll();
    }

    public void delete(UUID id) {
        dao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<T> getByCondition(Predicate<T> predicate) {
        return dao.getByCondition(predicate);
    }
}

