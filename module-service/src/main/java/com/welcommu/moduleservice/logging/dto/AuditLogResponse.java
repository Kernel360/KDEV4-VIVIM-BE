package com.welcommu.moduleservice.logging.dto;

import com.welcommu.moduledomain.logging.AuditLog;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private Long actorId;
    private String targetType;
    private Long targetId;
    private String actionType;
    private LocalDateTime loggedAt;
    private List<AuditLogDetailResponse> details;

    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
            log.getId(),
            log.getActorId(),
            log.getTargetType().name(),
            log.getTargetId(),
            log.getActionType().name(),
            log.getLoggedAt(),
            log.getDetails().stream().map(AuditLogDetailResponse::from).toList()
        );
    }
}