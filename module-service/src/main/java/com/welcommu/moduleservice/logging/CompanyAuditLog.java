package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyAuditLog implements AuditableService<Company>{

    private static final Logger log = LoggerFactory.getLogger(CompanyAuditLog.class);
    private final AuditLogRepository auditLogRepository;

    @Override
    public void createAuditLog(Company entity, Long userId) {
        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.COMPANY)
            .targetId(entity.getId())
            .actionType(ActionType.CREATE)
            .loggedAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(Company before, Company after, Long userId) {
        Map<String, String[]> changedFields = compareChanges(before, after);

        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.COMPANY)
            .targetId(after.getId())
            .actionType(ActionType.MODIFY)
            .loggedAt(LocalDateTime.now())
            .build();

        CompanyAuditLog.log.info("log {}", log);
        changedFields.forEach((field, values) ->
            log.addDetail(field, values[0], values[1])
        );

        auditLogRepository.save(log);
    }

    @Override
    public void deleteAuditLog(Company entity, Long userId) {
        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.COMPANY)
            .targetId(entity.getId())
            .actionType(ActionType.DELETE)
            .loggedAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);
    }


    private Map<String, String[]> compareChanges(Company before, Company after) {
        Map<String, String[]> changes = new HashMap<>();

        if (!Objects.equals(before.getName(), after.getName())) {
            changes.put("name", new String[]{before.getName(), after.getName()});
        }

        if (!Objects.equals(before.getPhone(), after.getPhone())) {
            changes.put("phone", new String[]{before.getPhone(), after.getPhone()});
        }

        if (!Objects.equals(before.getBusinessNumber(), after.getBusinessNumber())) {
            changes.put("BusinessNumber", new String[]{before.getBusinessNumber(), after.getBusinessNumber()});
        }

        if (!Objects.equals(before.getEmail(), after.getEmail())) {
            changes.put("email", new String[]{before.getEmail(), after.getEmail()});
        }

        if (!Objects.equals(before.getCompanyRole(), after.getCompanyRole())) {
            changes.put("role", new String[]{
                String.valueOf(before.getCompanyRole()), String.valueOf(after.getCompanyRole())
            });
        }

        if (!Objects.equals(before.getAddress(), after.getAddress())) {
            changes.put("address", new String[]{before.getAddress(), after.getAddress()});
        }

        if (!Objects.equals(before.getCoOwner(), after.getCoOwner())) {
            changes.put("CoOwner", new String[]{before.getCoOwner(), after.getCoOwner()});
        }
        return changes;
    }
}
