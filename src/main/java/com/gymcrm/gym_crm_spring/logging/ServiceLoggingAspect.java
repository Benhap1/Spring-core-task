package com.gymcrm.gym_crm_spring.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    /**
     * Pointcut for all public methods in the service package.
     */
    @Pointcut("execution(public * com.gymcrm.gym_crm_spring.service..*(..))")
    public void serviceMethods() {}

    /**
     * Logs method call with arguments.
     */
    @Before("serviceMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Calling method: {} with arguments {}", methodName, Arrays.toString(args));
    }

    /**
     * Logs method return value.
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Method {} returned with value: {}", methodName, result);
    }

    /**
     * Logs method exception.
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("Method {} threw exception: {}", methodName, ex.getMessage(), ex);
    }
}
