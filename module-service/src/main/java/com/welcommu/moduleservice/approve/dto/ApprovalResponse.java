package com.welcommu.moduleservice.approve.dto;

import com.welcommu.moduledomain.approval.Approval;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApprovalResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static ApprovalResponse of(Approval approval) {
        return ApprovalResponse.builder()
            .id(approval.getId())
            .title(approval.getTitle())
            .content(approval.getContent())
            .createdAt(approval.getCreatedAt())
            .build();
    }
}
