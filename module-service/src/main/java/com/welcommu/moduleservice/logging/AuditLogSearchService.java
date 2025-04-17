package com.welcommu.moduleservice.logging;

import com.welcommu.modulerepository.logging.AuditLogRepository;
import com.welcommu.moduleservice.logging.dto.AuditLogResponse;
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

    public List<AuditLogResponse> getLogsByTargetId(Long targetId) {
        return auditLogRepository.findByTargetId(targetId).stream()
            .map(AuditLogResponse::from)
            .toList();
    }

}
