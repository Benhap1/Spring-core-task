//package com.gymcrm.gym_crm_spring.logging;
//
//import com.gymcrm.gym_crm_spring.domain.Trainer;
//import com.gymcrm.gym_crm_spring.service.TrainerService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class LoggingAspectTest {
//
//    @Test
//    void aspectLogsMethodCall() {
//        // given: TrainerService proxied with ServiceLoggingAspect
//        TrainerService target = Mockito.mock(TrainerService.class);
//        Mockito.when(target.listAll()).thenReturn(List.of(
//                Trainer.builder()
//                        .id(UUID.randomUUID()) // ✅ заменили String → UUID
//                        .firstName("John")
//                        .lastName("Smith")
//                        .specialization("Cardio")
//                        .build()
//        ));
//
//        AspectJProxyFactory factory = new AspectJProxyFactory(target);
//        factory.addAspect(ServiceLoggingAspect.class);
//        TrainerService proxy = factory.getProxy();
//
//        // when
//        List<Trainer> result = proxy.listAll();
//
//        // then
//        assertNotNull(result);
//        assertEquals(1, result.size());
//
//        // Checking method is called or not
//        Mockito.verify(target, Mockito.times(1)).listAll();
//    }
//}
