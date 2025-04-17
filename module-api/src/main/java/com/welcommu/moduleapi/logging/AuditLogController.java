package com.welcommu.moduleapi.logging;

import com.welcommu.moduledomain.logging.AuditLog;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduleservice.logging.AuditLogSearchService;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auditLog")
@Tag(name = "Log API", description = "각 Entity별 로그를 확인합니다.")
public class AuditLogController {

    private final AuditLogSearchService auditLogSearchService;

    @GetMapping
    @Operation(summary = "감사 로그 전체 조회 (관리자용)")
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        List<AuditLogResponse> logs = auditLogSearchService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

}
