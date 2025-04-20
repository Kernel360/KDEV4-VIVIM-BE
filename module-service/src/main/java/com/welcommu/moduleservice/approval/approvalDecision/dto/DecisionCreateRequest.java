package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.*;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DecisionCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ApprovalDecisionStatus decisionStatus;  // APPROVED or REJECTED

    public ApprovalDecision toEntity(ApprovalApprover approvalApprover) {
        return ApprovalDecision.builder()
            .title(this.title)
            .content(this.content)
            .decidedAt(LocalDateTime.now())
            .approvalApprover(approvalApprover)
            .decisionStatus(this.decisionStatus)
            .build();
    }
}
