package com.gymcrm.gym_crm_spring.service;

import com.gymcrm.gym_crm_spring.dao.UserDao;
import com.gymcrm.gym_crm_spring.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends AbstractService<User> {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}