package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproverResponse {

    private Long userId;
    private String name;

    public static ApproverResponse of(ApprovalApprover approver) {
        return ApproverResponse.builder()
            .userId(approver.getProjectUser().getUser().getId())
            .name(approver.getProjectUser().getUser().getName())
            .build();
    }
}
