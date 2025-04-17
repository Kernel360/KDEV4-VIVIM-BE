package com.welcommu.moduleservice.logging;


import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.AuditLogDetail;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.modulerepository.logging.AuditLogDetailRepository;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProjectAuditService implements AuditableService<Project> {

    private static final Logger log = LoggerFactory.getLogger(ProjectAuditService.class);
    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;

    @Override
    public void createAuditLog(Project entity, Long userId) {
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
    public void modifyAuditLog(Project before, Project after, Long userId) {
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);


        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.PROJECT)
            .targetId(after.getId())
            .actionType(ActionType.MODIFY)
            .loggedAt(LocalDateTime.now())
            .build();

        changedFields.forEach((field, values) ->
            log.addDetail(field, values[0], values[1])
        );

        auditLogRepository.save(log);
    }

    @Override
    public void deleteAuditLog(Project entity, Long userId) {
        AuditLog log = AuditLog.builder()
                .actorId(userId)
                .targetType(TargetType.PROJECT)
                .targetId(entity.getId())
                .actionType(ActionType.DELETE)
                .loggedAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }
}
