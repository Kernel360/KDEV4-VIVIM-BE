package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.UserSnapshot;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuditService implements AuditableService<UserSnapshot>{
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
        AuditLog log = auditLogFactory.create(TargetType.USER, after.getId(), ActionType.MODIFY, userId);
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);
        changedFields.forEach((field, values) ->
            log.addDetail(field, values[0], values[1])
        );

        auditLogRepository.save(log);

    }

    @Override
    public void deleteAuditLog(UserSnapshot user, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.USER, user.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
