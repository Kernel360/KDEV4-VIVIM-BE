package com.welcommu.modulerepository.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepositoryCustom {
    List<AuditLog> findByConditions(
        ActionType actionType,
        TargetType targetType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long userId
    );
}