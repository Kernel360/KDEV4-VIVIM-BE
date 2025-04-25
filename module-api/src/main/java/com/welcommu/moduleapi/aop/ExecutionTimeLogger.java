package com.welcommu.moduleapi.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLogger {
    @Around("execution(* com.welcommu.moduleapi..*Controller.*(..)) || execution(* com.welcommu.moduleservice..*Service.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        log.info("⏱️ {} executed in {} ms", joinPoint.getSignature(), duration);

        return result;
    }
}
