package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DecisionModifyRequest {

    private String title;
    private String content;

    @NotNull
    private ApprovalDecisionStatus decisionStatus;
}
