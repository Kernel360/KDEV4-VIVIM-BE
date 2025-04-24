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
    private String title;
    private String content;
    private ApprovalDecisionStatus status;
    private Long userId; // 승인권자 식별
    private Long approverId;
    private String approverName;
    private LocalDateTime decidedAt;

    public static DecisionResponse from(ApprovalDecision decision) {
        return DecisionResponse.builder()
            .id(decision.getId())
            .title(decision.getTitle())
            .title(decision.getContent())
            .status(decision.getDecisionStatus())
            .approverId(decision.getApprovalApprover().getId())
            .approverName(decision.getApprovalApprover().getProjectUser().getUser().getName())
            .decidedAt(decision.getDecidedAt())
            .build();
    }
}
