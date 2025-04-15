package com.welcommu.modulecommon.logging.aspect;

import com.welcommu.modulecommon.logging.LogAudit;
import com.welcommu.modulecommon.logging.entity.AuditLog;
import com.welcommu.modulecommon.logging.entity.AuditLogDetail;
import com.welcommu.modulecommon.logging.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    @Around("@annotation(logAudit)")
    public Object logAuditProcess(ProceedingJoinPoint pjp, LogAudit logAudit) throws Throwable {
        Object[] args = pjp.getArgs();

        Object beforeEntity = getEntityFromArgs(args);

        log.info("berforeEntity {}", beforeEntity);

        Object backupBefore = deepCopy(beforeEntity); // optional: 값을 미리 복사

        Object result = pjp.proceed(); // 실제 메서드 실행

        Object afterEntity = getEntityFromArgs(args);

        Map<String, String[]> changedFields = compareFields(backupBefore, afterEntity);

        Long actorId = extractUserId();
        Long targetId = extractEntityId(afterEntity);

        AuditLog auditLog = AuditLog.builder()
                .actorId(actorId)
                .targetType(logAudit.targetType())
                .targetId(targetId)
                .actionType(logAudit.actionType())
                .loggedAt(LocalDateTime.now())
                .build();

        for (Map.Entry<String, String[]> entry : changedFields.entrySet()) {
            auditLog.getDetails().add(
                    AuditLogDetail.builder()
                            .auditLog(auditLog)
                            .fieldName(entry.getKey())
                            .oldValue(entry.getValue()[0])
                            .newValue(entry.getValue()[1])
                            .build()
            );
        }

        auditLogRepository.save(auditLog);
        return result;
    }

    private Object getEntityFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg != null) {
                for (Field field : arg.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(jakarta.persistence.Id.class)) {
                        return arg;
                    }
                }
            }
        }
        return null;
    }

    private Long extractEntityId(Object entity) {
        try {
            log.info("ID 확인용 entity {}", entity);
            return (Long) entity.getClass().getMethod("userId").invoke(entity);
        } catch (Exception e) {
            throw new IllegalStateException("⚠️ ID 추출 실패", e);
        }
    }

    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;

        String name = authentication.getName(); // 우리가 token에 userId를 넣은 경우
        try {
            return Long.parseLong(name);
        } catch (NumberFormatException e) {
            log.warn("userId 추출 실패 (name = {})", name);
            return null;
        }
    }

    private Map<String, String[]> compareFields(Object before, Object after) {
        Map<String, String[]> changes = new HashMap<>();
        if (before == null || after == null) return changes;

        for (Field field : before.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object beforeValue = field.get(before);
                Object afterValue = field.get(after);

                if (!Objects.equals(beforeValue, afterValue)) {
                    changes.put(field.getName(),
                            new String[]{String.valueOf(beforeValue), String.valueOf(afterValue)});
                }

            } catch (IllegalAccessException e) {
                log.warn("필드 비교 실패: {}", field.getName());
            }
        }

        return changes;
    }

    // Optional: 깊은 복사 대신 shallow copy 용도
    private Object deepCopy(Object original) {
        // 이 부분은 추후 필요 시 구현 (직렬화나 모델 복사 유틸 사용)
        return original;
    }
}
