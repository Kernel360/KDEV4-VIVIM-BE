package com.welcommu.moduleservice.logging.dto;

import com.welcommu.moduledomain.logging.AuditLogDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuditLogDetailResponse {
    private String fieldName;
    private String oldValue;
    private String newValue;

    public static AuditLogDetailResponse from(AuditLogDetail detail) {
        return new AuditLogDetailResponse(
                detail.getFieldName(),
        detail.getOldValue(),
        detail.getNewValue()
        );
    }
}