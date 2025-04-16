package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.modulerepository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuditService implements AuditableService<User>{
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    public void createAuditLog(User entity, Long userId) {
        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.USER)
            .targetId(entity.getId())
            .actionType(ActionType.CREATE)
            .loggedAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);
    }

    @Override
    public void modifyAuditLog(User before, User after, Long userId) {
        Map<String, String[]> changedFields = compareChanges(before, after);

        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.USER)
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
    public void deleteAuditLog(User user, Long userId) {
        AuditLog log = AuditLog.builder()
            .actorId(userId)
            .targetType(TargetType.USER)
            .targetId(user.getId())
            .actionType(ActionType.DELETE)
            .loggedAt(LocalDateTime.now())
            .build();
        auditLogRepository.save(log);
    }

    private Map<String, String[]> compareChanges(User before, User after) {
        Map<String, String[]> changes = new HashMap<>();

        if (!Objects.equals(before.getName(), after.getName())) {
            changes.put("name", new String[]{before.getName(), after.getName()});
        }

        if (!Objects.equals(before.getPhone(), after.getPhone())) {
            changes.put("phone", new String[]{before.getPhone(), after.getPhone()});
        }

        if (!Objects.equals(before.getEmail(), after.getEmail())) {
            changes.put("email", new String[]{before.getEmail(), after.getEmail()});
        }

        if (!Objects.equals(before.getRole(), after.getRole())) {
            changes.put("role", new String[]{
                String.valueOf(before.getRole()), String.valueOf(after.getRole())
            });
        }

        if (!Objects.equals(before.getCompany(), after.getCompany())) {
            changes.put("company", new String[]{before.getCompany().getName(), after.getCompany().getName()});
        }

        log.info("changes {}", changes);

        return changes;
    }
}
