package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AuditLogFactory {
    public AuditLog create(TargetType targetType, Long targetId, ActionType actionType, Long actorId) {
        return AuditLog.builder()
            .actorId(actorId)
            .targetType(targetType)
            .targetId(targetId)
            .actionType(actionType)
            .loggedAt(LocalDateTime.now())
            .build();
    }

    public AuditLog createWithDetails(TargetType type, Long targetId, ActionType action, Long actorId,
        Map<String, String[]> fields) {
        AuditLog log = create(type, targetId, action, actorId);
        for (Map.Entry<String, String[]> entry : fields.entrySet()) {
            log.addDetail(entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
        }
        return log;
    }
}
