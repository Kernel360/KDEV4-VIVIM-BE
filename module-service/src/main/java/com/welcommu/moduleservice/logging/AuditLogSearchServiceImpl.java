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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        // (A) 파싱
        LocalDateTime start = startDate != null
            ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = endDate != null
            ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        // (B) ID만 커서로 조회
        List<Long> ids = auditLogRepository.findIdsWithCursor(
            actionType, targetType, start, end, userId,
            cursorLoggedAt, cursorId, size
        );
        if (ids.isEmpty()) {
            return LogsWithCursor.<AuditLogResponse>builder()
                .logs(List.of())
                .next(null)
                .currentPage(1)
                .totalPages(1)
                .build();
        }

        // (C) 상세 fetch
        List<AuditLog> entities = auditLogRepository.findWithDetailsByIds(ids);
        List<AuditLogResponse> dtos = entities.stream()
            .map(AuditLogResponse::from)
            .toList();

        // (D) 페이지 번호 계산
        long totalCount = auditLogRepository.countByFilters(
            actionType, targetType, start, end, userId
        );
        int totalPages = (int) Math.ceil((double) totalCount / size);

        long beforeCount = 0;
        if (cursorLoggedAt != null && cursorId != null) {
            beforeCount = auditLogRepository.countBeforeCursor(
                actionType, targetType, start, end, userId,
                cursorLoggedAt, cursorId
            );
        }
        int currentPage = (int)(beforeCount / size) + 1;

        // (E) next cursor
        AuditLogResponse last = dtos.get(dtos.size() - 1);
        Cursor next = new Cursor(last.getLoggedAt().toString(), last.getId());

        return LogsWithCursor.<AuditLogResponse>builder()
            .logs(dtos)
            .next(next)
            .currentPage(currentPage)
            .totalPages(totalPages)
            .build();
    }

    public LogsWithCursor<AuditLogResponse> searchLogsByPage(
        ActionType actionType,
        TargetType targetType,
        String startDate,
        String endDate,
        Long userId,
        int page,
        int size
    ) {
        // 날짜 파싱 (cursor 모드와 동일)
        LocalDateTime start = startDate != null
            ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = endDate != null
            ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        // 1) 필터+페이지 요청으로 ID만 뽑기
        Pageable pageable = PageRequest.of(page - 1, size,
            Sort.by("loggedAt").descending().and(Sort.by("id").descending())
        );
        List<Long> ids = auditLogRepository.findIdsByFilters(
            actionType, targetType, start, end, userId, pageable
        );
        if (ids.isEmpty()) {
            return new LogsWithCursor<>(List.of(), null, page, 0);
        }

        // 2) 상세 fetch
        List<AuditLog> entities = auditLogRepository.findWithDetailsByIds(ids);
        List<AuditLogResponse> dtos = entities.stream()
            .map(AuditLogResponse::from)
            .toList();

        // 3) 카운트로 totalPages 계산
        long totalCount = auditLogRepository.countByFilters(
            actionType, targetType, start, end, userId
        );
        int totalPages = (int)Math.ceil((double)totalCount / size);

        // 4) next cursor는 마지막 아이템
        AuditLogResponse last = dtos.get(dtos.size() - 1);
        Cursor next = new Cursor(last.getLoggedAt().toString(), last.getId());

        return new LogsWithCursor<>(dtos, next, page, totalPages);
    }
}
