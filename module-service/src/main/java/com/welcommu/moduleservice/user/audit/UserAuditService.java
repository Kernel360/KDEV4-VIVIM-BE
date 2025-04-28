package com.welcommu.moduleservice.user.audit;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleinfra.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.AuditLogFactory;
import com.welcommu.moduleservice.logging.AuditLogFieldComparator;
import com.welcommu.moduleservice.logging.AuditableService;
import com.welcommu.moduleservice.user.dto.UserSnapshot;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuditService implements AuditableService<UserSnapshot> {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;
    private final AuditLogFactory auditLogFactory;

    @Override
    public void createAuditLog(UserSnapshot user, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.USER, user.getId(), ActionType.CREATE, userId);
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(UserSnapshot before, UserSnapshot after, Long userId) {
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);
        AuditLog log = auditLogFactory.createWithFieldChanges(TargetType.USER, after.getId(), ActionType.MODIFY, userId, changedFields);
        auditLogRepository.save(log);

    }

    @Override
    public void deleteAuditLog(UserSnapshot user, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.USER, user.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
