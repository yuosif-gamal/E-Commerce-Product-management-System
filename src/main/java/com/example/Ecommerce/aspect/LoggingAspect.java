package com.example.Ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all methods in any service class
    @Pointcut("execution(* com.example.Ecommerce.service.*.*(..))")
    public void serviceMethods() {}

    // Around advice: Runs before and after the method execution
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            // Proceed with the method execution
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.info("Method {} completed successfully in {} ms with result: {}",
                    joinPoint.getSignature().getName(), executionTime, result);

            return result;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.error("Method {} threw exception in {} ms: {}",
                    joinPoint.getSignature().getName(), executionTime, throwable.getMessage());
            throw throwable;
        }
    }

    // Before advice: Runs before each method execution
    @Before("serviceMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        LOGGER.info("Entering method: {}", joinPoint.getSignature().getName());
    }

    // AfterReturning advice: Runs after the method completes successfully
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info("Method {} completed successfully with result: {}", joinPoint.getSignature().getName(), result);
    }

    // AfterThrowing advice: Runs if the method throws an exception
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Method {} threw exception: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }

    // After advice: Runs after the method execution (whether success or failure)
    @After("serviceMethods()")
    public void logAfterMethod(JoinPoint joinPoint) {
        LOGGER.info("Exiting method: {}", joinPoint.getSignature().getName());
    }
}
