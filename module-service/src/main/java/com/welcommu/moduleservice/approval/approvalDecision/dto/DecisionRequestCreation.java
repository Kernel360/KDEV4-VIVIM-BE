package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DecisionRequestCreation {
    
    @NotBlank
    private String content;
    
    @NotNull
    private ApprovalDecisionStatus decisionStatus;  // APPROVED, REJECTED, PENDING
    
    public ApprovalDecision toEntity(ApprovalApprover approvalApprover) {
        return ApprovalDecision.builder().content(this.content).decidedAt(LocalDateTime.now())
            .approvalApprover(approvalApprover).decisionStatus(this.decisionStatus).build();
    }
}
