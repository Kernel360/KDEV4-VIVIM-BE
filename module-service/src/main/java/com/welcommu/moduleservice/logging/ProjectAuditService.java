package com.welcommu.moduleservice.logging;


import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.ProjectSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProjectAuditService implements AuditableService<ProjectSnapshot>{

    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;
    private final AuditLogFactory auditLogFactory;

    @Override
    public void createAuditLog(ProjectSnapshot project, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.PROJECT, project.getId(), ActionType.CREATE, userId);
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(ProjectSnapshot before, ProjectSnapshot after, Long userId) {
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);
        AuditLog log = auditLogFactory.createWithDetails(TargetType.PROJECT, after.getId(), ActionType.MODIFY, userId,changedFields);
        auditLogRepository.save(log);
    }

    @Override
    public void deleteAuditLog(ProjectSnapshot project, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.PROJECT, project.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
