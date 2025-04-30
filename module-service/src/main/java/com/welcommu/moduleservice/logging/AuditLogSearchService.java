package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
import com.welcommu.moduleservice.logging.dto.LogsWithCursor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogSearchService {
    List<AuditLogResponse> getAllLogs();
    Page<AuditLogResponse> searchLogs(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId,
        Pageable pageable
    );
    LogsWithCursor<AuditLogResponse> searchLogs(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId,
        LocalDateTime cursorLoggedAt,
        Long cursorId,
        int size
    );
}
