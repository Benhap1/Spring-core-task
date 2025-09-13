package com.gymcrm.gym_crm_spring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GymCrmSpringApplicationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> GymCrmSpringApplication.main(new String[]{}));
    }
}
