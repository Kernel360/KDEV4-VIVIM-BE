package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.CompanySnapshot;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyAuditService implements AuditableService<CompanySnapshot>{

    private static final Logger log = LoggerFactory.getLogger(CompanyAuditService.class);
    private final AuditLogRepository auditLogRepository;
    private final AuditLogFieldComparator auditLogFieldComparator;
    private final AuditLogFactory auditLogFactory;

    @Override
    public void createAuditLog(CompanySnapshot company, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.COMPANY, company.getId(), ActionType.CREATE, userId);
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(CompanySnapshot before, CompanySnapshot after, Long userId) {
        Map<String, String[]> changedFields = auditLogFieldComparator.compare(before, after);
        AuditLog log = auditLogFactory.createWithDetails(TargetType.COMPANY, after.getId(), ActionType.MODIFY, userId,changedFields);
        auditLogRepository.save(log);
    }
    @Override
    public void deleteAuditLog(CompanySnapshot company, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.COMPANY, company.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
