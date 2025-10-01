package com.gymcrm.gym_crm_spring.logging;

import com.gymcrm.gym_crm_spring.domain.Trainer;
import com.gymcrm.gym_crm_spring.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class LoggingAspectTest {

    @Test
    void aspectLogsMethodCall() {
        TrainerService target = mock(TrainerService.class);

        when(target.findAll()).thenReturn(List.of(
                Trainer.builder()
                        .id(UUID.randomUUID())
                        .user(null)
                        .specialization(null)
                        .build()
        ));

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(ServiceLoggingAspect.class);
        TrainerService proxy = factory.getProxy();

        List<Trainer> result = proxy.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(target, times(1)).findAll();
    }
}
