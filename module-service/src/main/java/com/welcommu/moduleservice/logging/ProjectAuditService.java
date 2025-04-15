package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.AuditLogDetail;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.modulerepository.logging.AuditLogDetailRepository;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProjectAuditService implements AuditableService<Project> {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void logCreateAudit(Project entity, Long userId) {
        AuditLog log = AuditLog.builder()
                .actorId(userId)
                .targetType(TargetType.PROJECT)
                .targetId(entity.getId())
                .actionType(ActionType.CREATE)
                .loggedAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Override
    public void logUpdateAudit(Project before, Project after, Long userId) {
        AuditLog log = AuditLog.builder()
                .actorId(userId)
                .targetType(TargetType.PROJECT)
                .targetId(after.getId())
                .actionType(ActionType.MODIFY)
                .loggedAt(LocalDateTime.now())
                .details(new ArrayList<>())
                .build();

        List<AuditLogDetail> details = compareChanges(before, after).entrySet().stream()
                .map(entry -> AuditLogDetail.builder()
                        .auditLog(log)
                        .fieldName(entry.getKey())
                        .oldValue(entry.getValue()[0])
                        .newValue(entry.getValue()[1])
                        .build())
                .toList();

        log.getDetails().addAll(details);
        auditLogRepository.save(log);
    }

    @Override
    public void logDeleteAudit(Project entity, Long userId) {
        AuditLog log = AuditLog.builder()
                .actorId(userId)
                .targetType(TargetType.PROJECT)
                .targetId(entity.getId())
                .actionType(ActionType.DELETE)
                .loggedAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    private Map<String, String[]> compareChanges(Project before, Project after) {
        Map<String, String[]> changes = new HashMap<>();
        if (!Objects.equals(before.getName(), after.getName())) {
            changes.put("name", new String[]{before.getName(), after.getName()});
        }
        if (!Objects.equals(before.getDescription(), after.getDescription())) {
            changes.put("description", new String[]{before.getDescription(), after.getDescription()});
        }
        return changes;
    }
}
