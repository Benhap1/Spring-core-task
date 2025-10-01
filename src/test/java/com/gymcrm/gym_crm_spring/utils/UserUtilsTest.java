package com.gymcrm.gym_crm_spring.utils;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UserUtilsTest {

    @Test
    void generateUsername_unique() {
        String username = UserUtils.generateUsername("John", "Smith", List.of());
        assertEquals("john.smith", username);
    }

    @Test
    void generateUsername_withCollision_addsNumber() {

        User existingUser = User.builder()
                .firstName("John")
                .lastName("Smith")
                .username("john.smith")
                .build();

        Trainer existing = Trainer.builder()
                .user(existingUser)
                .build();

        String username = UserUtils.generateUsername("John", "Smith", List.of(existing.getUser()));

        assertTrue(username.startsWith("john.smith"));
        assertNotEquals("john.smith", username);
    }

    @Test
    void generatePassword_lengthIs10() {
        String password = UserUtils.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generatePassword_isRandom() {
        String p1 = UserUtils.generatePassword();
        String p2 = UserUtils.generatePassword();
        assertNotEquals(p1, p2);
    }
}
