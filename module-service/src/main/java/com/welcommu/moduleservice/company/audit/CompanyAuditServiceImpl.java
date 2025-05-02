package com.welcommu.moduleservice.company.audit;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleinfra.logging.AuditLogRepository;
import com.welcommu.moduleservice.company.dto.CompanySnapshot;
import com.welcommu.moduleservice.logging.AuditLogFactory;
import com.welcommu.moduleservice.logging.AuditLogFieldComparator;
import com.welcommu.moduleservice.logging.AuditLogFieldComparatorImpl;
import com.welcommu.moduleservice.logging.AuditableService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyAuditServiceImpl implements AuditableService<CompanySnapshot>, CompanyAuditService {

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
        AuditLog log = auditLogFactory.createWithFieldChanges(TargetType.COMPANY, after.getId(), ActionType.MODIFY, userId,changedFields);
        auditLogRepository.save(log);
    }
    @Override
    public void deleteAuditLog(CompanySnapshot company, Long userId) {
        AuditLog log = auditLogFactory.create(TargetType.COMPANY, company.getId(), ActionType.DELETE, userId);
        auditLogRepository.save(log);
    }
}
