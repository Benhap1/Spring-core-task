package com.gymcrm.gym_crm_spring.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    /**
     * Logs all public methods in the service package.
     */
    @Around("execution(public * com.gymcrm.gym_crm_spring.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Calling method: {} with arguments {}", methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("Method {} completed successfully in {} ms, result = {}", methodName, duration, result);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("Error in method {} after {} ms: {}", methodName, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
