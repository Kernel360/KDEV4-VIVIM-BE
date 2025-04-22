package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproverResponse {
    
    private Long approverId;
    private Long userId;
    private String name;
    private String decisionStatus; // PENDING, APPROVED, REJECTED
    private String latestDecisionTitle;
    private String latestDecisionContent;
    private LocalDateTime latestDecidedAt;
    
    public static ApproverResponse from(ApprovalApprover approver,
        ApprovalDecision latestDecision) {
        String status;
        
        if (latestDecision == null) {
            status = "PENDING";
        } else if (latestDecision.getDecisionStatus() == ApprovalDecisionStatus.APPROVED) {
            status = "APPROVED";
        } else {
            status = "REJECTED";
        }
        
        return ApproverResponse.builder().approverId(approver.getId())
            .userId(approver.getProjectUser().getUser().getId())
            .name(approver.getProjectUser().getUser().getName()).decisionStatus(status)
            .latestDecisionTitle(latestDecision != null ? latestDecision.getTitle() : null)
            .latestDecisionContent(latestDecision != null ? latestDecision.getContent() : null)
            .latestDecidedAt(latestDecision != null ? latestDecision.getDecidedAt() : null).build();
    }
}
