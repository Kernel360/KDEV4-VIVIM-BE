package com.welcommu.moduleapi.logging;

import com.welcommu.moduledomain.logging.enums.ActionType;
import com.welcommu.moduledomain.logging.enums.TargetType;
import com.welcommu.moduleservice.logging.AuditLogSearchService;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
import com.welcommu.moduleservice.logging.dto.LogsWithCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auditLog")
@Tag(name = "Log API", description = "각 Entity별 로그를 확인합니다.")
public class AuditLogController {

    private final AuditLogSearchService auditLogSearchService;

    @GetMapping
    @Operation(summary = "감사 로그 전체 조회")
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        List<AuditLogResponse> logs = auditLogSearchService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/search")
    @Operation(summary = "감사 로그 검색")
    public ResponseEntity<Page<AuditLogResponse>> searchAuditLogs(
        @RequestParam(required = false) ActionType actionType,
        @RequestParam(required = false) TargetType entityType,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) Long userId,
        Pageable pageable
    ) {
        Page<AuditLogResponse> logs = auditLogSearchService.searchLogs(actionType, entityType, startDate, endDate, userId, pageable);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/searchCursor")
    public ResponseEntity<LogsWithCursor<AuditLogResponse>> search(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) ActionType actionType,
        @RequestParam(required = false) TargetType targetType,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) LocalDateTime cursorLoggedAt,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "10") int size
    ) {
        LogsWithCursor<AuditLogResponse> result;
        if (page != null) {
            result = auditLogSearchService.searchLogsByPage(
                actionType, targetType, startDate, endDate, userId,
                page, size
            );
        } else {
            result = auditLogSearchService.searchLogs(
                actionType, targetType, startDate, endDate, userId,
                cursorLoggedAt, cursorId, size
            );
        }
        return ResponseEntity.ok(result);
    }

}
