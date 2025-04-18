package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogSearchService {
    private final AuditLogRepository auditLogRepository;

    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
            .map(AuditLogResponse::from)
            .toList();
    }

    public List<AuditLogResponse> searchLogs(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId
    ) {
        LocalDateTime start = (startDate != null) ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        List<AuditLog> logs = auditLogRepository.findByConditions(actionType, targetType, start, end, userId);

        return logs.stream()
            .map(AuditLogResponse::from) // 🔥 기존 DTO 활용
            .toList();
    }

}
