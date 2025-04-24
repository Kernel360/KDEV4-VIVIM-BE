package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecisionResponseSend {
    
    private String message;
    
    public static DecisionResponseSend from(User user, ApprovalDecision decision) {
        String companyName = user.getCompany().getName();
        String userName = user.getName();
        String proposalTitle = decision.getApprovalApprover().getApprovalProposal().getTitle();
        ApprovalDecisionStatus decisionStatus = decision.getDecisionStatus();
        
        String message = String.format("[승인응답] %s 고객사 소속 %s님이 \"%s\"에 대해 %s 응답을 보냈습니다.",
            companyName, userName, proposalTitle, decisionStatus);
        
        return new DecisionResponseSend(message);
    }
}
