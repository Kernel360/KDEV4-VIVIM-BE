package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalApproverStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproverResponse {

    private Long approverId;
    private Long userId;
    private String name;
    private String approverStatus;

    public static ApproverResponse from(ApprovalApprover approver) {
        return ApproverResponse.builder()
            .approverId(approver.getId())
            .userId(approver.getProjectUser().getUser().getId())
            .name(approver.getProjectUser().getUser().getName())
            .approverStatus(approver.getApproverStatus().toString())
            .build();
    }
}