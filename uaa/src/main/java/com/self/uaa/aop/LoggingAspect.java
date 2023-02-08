package com.self.uaa.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("within(com.self.uaa.controller.rest.*)")
    public void pointcut() {
        // Define pointcut
    }

    @Before("pointcut() && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void logBeforePostMethod(JoinPoint joinPoint) {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter: {}.{}() with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        } else {
            logger.info("Enter: {}.{}() with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }
    }

    @AfterReturning(
            pointcut = "pointcut() && @annotation(org.springframework.web.bind.annotation.PostMapping)",
            returning = "entity"
    )
    public void logAfterReturningPostMethod(JoinPoint joinPoint, ResponseEntity<?> entity) {
        try {
            logger.info("Exit: {}.{}() with result = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    entity);
        } catch (IllegalArgumentException exception) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw exception;
        }
    }

    @AfterThrowing(
            pointcut = "pointcut() && @annotation(org.springframework.web.bind.annotation.PostMapping)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error(
                "Exception in {}.{}() with cause: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Objects.isNull(exception.getCause()) ? "NULL" : exception.getCause());
    }
}
