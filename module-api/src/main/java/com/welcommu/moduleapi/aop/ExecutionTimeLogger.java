package com.welcommu.moduleapi.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutionTimeLogger {

    @Value("${logging.execution-time.enabled:false}")
    private boolean enabled;

    @Around("execution(* com.welcommu.moduleapi..*Controller.*(..)) || execution(* com.welcommu.moduleservice..*Service.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabled) {
            return joinPoint.proceed();
        }

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("⏱️ {} executed in {} ms", joinPoint.getSignature(), duration);
        return result;
    }
}
