package com.enm.whereToLive.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_RETURN_VALUE_LENGTH = 1000; // 리턴 값 최대 길이

    //컨트롤러 포인트 컷
    @Pointcut("execution(* com.enm.whereToLive.controller..*.*(..))")
    private void cut() {}

    // 새로운 포인트컷: 서비스와 리포지토리 메서드
    @Pointcut("execution(* com.enm.whereToLive.service..*.*(..)) && " +
            "!execution(* com.enm.whereToLive.service.StationService.*(..)) || " +
            "execution(* com.enm.whereToLive.repository..*.*(..))")
    private void serviceAndRepositoryCut() {}

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        logMethodEntry(method, joinPoint.getArgs());
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        logMethodExit(returnObj, method);
    }

    @Around("serviceAndRepositoryCut()")
    public Object aroundLogServiceAndRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint);
    }

    private void logMethodEntry(Method method, Object[] args) {
        log.info("======= Entering method: {} =======", method.getName());
        Map<String, Object> parametersMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            String paramName = parameters[i].getName();
            Object paramValue = args[i];
            parametersMap.put(paramName, paramValue);
        }
        try {
            String parametersJson = objectMapper.writeValueAsString(parametersMap);
            log.info("Parameters: {}", parametersJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize parameters", e);
        }
    }

    private void logMethodExit(Object returnObj, Method method) {
        try {
            String returnValueJson = objectMapper.writeValueAsString(returnObj);
            // 리턴 값의 길이를 제한
            if (returnValueJson.length() > MAX_RETURN_VALUE_LENGTH) {
                returnValueJson = returnValueJson.substring(0, MAX_RETURN_VALUE_LENGTH) + "... [truncated]";
            }
            log.info("Return value: {}", returnValueJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize return value", e);
        }
        log.info("======= Exiting method: {} =======", method.getName());
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        // 메서드 실행
        Object returnObj = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime; // 실행 시간 계산
        log.info("Executed {}.{} in {} ms", method.getDeclaringClass().getSimpleName(), method.getName(), executionTime);

        return returnObj; // 메서드의 반환값 반환
    }
}
