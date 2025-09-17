package com.gymcrm.gym_crm_spring.utils;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {

    @Test
    void generateUsername_unique() {
        String username = UserUtils.generateUsername("John", "Smith", List.of());
        assertEquals("john.smith", username);
    }

    @Test
    void generateUsername_withCollision_addsNumber() {
        Trainer existing = Trainer.builder().firstName("John").lastName("Smith").username("John.Smith").build();

        String username = UserUtils.generateUsername("John", "Smith", List.of(existing));

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
