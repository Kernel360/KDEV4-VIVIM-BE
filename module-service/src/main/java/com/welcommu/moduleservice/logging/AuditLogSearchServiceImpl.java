package com.welcommu.moduleservice.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleinfra.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
import com.welcommu.moduleservice.logging.dto.Cursor;
import com.welcommu.moduleservice.logging.dto.LogsWithCursor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogSearchServiceImpl implements AuditLogSearchService{

    private final AuditLogRepository auditLogRepository;

    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
            .map(AuditLogResponse::from)
            .toList();
    }

    public Page<AuditLogResponse> searchLogs(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId,
        Pageable pageable
    ) {
        LocalDateTime start =
            (startDate != null) ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end =
            (endDate != null) ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        Page<AuditLog> logs = auditLogRepository.findByConditions(
            actionType, targetType, start, end, userId, pageable
        );
        return logs.map(AuditLogResponse::from);
    }

    public LogsWithCursor<AuditLogResponse> searchLogs(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId,
        LocalDateTime cursorLoggedAt,
        Long cursorId,
        int size
    ) {
        // (A) start/end 파싱
        LocalDateTime start = startDate != null
            ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = endDate != null
            ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        // (B) 1단계: ID만 조회
        List<Long> ids = auditLogRepository.findIdsWithCursor(
            actionType, targetType, start, end, userId,
            cursorLoggedAt, cursorId, size
        );
        if (ids.isEmpty()) {
            return new LogsWithCursor<>(List.of(), null);
        }

        // (C) 2단계: Fetch Join 으로 상세까지 한 번에 로드
        List<AuditLog> entities = auditLogRepository.findWithDetailsByIds(ids);

        // (D) DTO 변환 + nextCursor 계산
        List<AuditLogResponse> dtos = entities.stream()
            .map(AuditLogResponse::from)
            .toList();

        AuditLogResponse last = dtos.get(dtos.size() - 1);
        Cursor next = new Cursor(
            last.getLoggedAt().toString(), last.getId()
        );
        return new LogsWithCursor<>(dtos, next);

    }
}
