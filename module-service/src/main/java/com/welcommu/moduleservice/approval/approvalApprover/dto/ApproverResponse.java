package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApproverResponse {

    private Long userId;
    private String name;
    private boolean isApproved;
    private LocalDateTime approvedAt;

    public static ApproverResponse of(ApprovalApprover approver) {
        return ApproverResponse.builder()
                .userId(approver.getProjectUser().getUser().getId())
                .name(approver.getProjectUser().getUser().getName())
                .isApproved(approver.isApproved())
                .approvedAt(approver.getApprovedAt())
                .build();
    }
}
