package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DecisionResponse {

    private Long id;
    private String content;
    private ApprovalDecisionStatus status;
    private Long approverId;
    private String approverName;
    private LocalDateTime decidedAt;

    public static DecisionResponse from(ApprovalDecision decision) {

        return DecisionResponse.builder()
            .id(decision.getId())
            .content(decision.getContent())
            .status(decision.getDecisionStatus())
            .approverId(decision.getApprovalApprover()
                .getId())
            .approverName(decision.getApprovalApprover()
                .getProjectUser()
                .getUser()
                .getName())
            .decidedAt(decision.getDecidedAt())
            .build();
    }
}
