package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 하나의 승인요청(ApprovalProposal)에 대한 승인권자들의 응답정보
 */

@Getter
@Builder
public class DecisionResponseByFilteredApprover {
    
    private Long proposalId;
    private String title;
    private List<DecisionResponseByApproverList> approvers;
    
    public static DecisionResponseByFilteredApprover from(ApprovalProposal proposal,
        List<DecisionResponseByApproverList> approvers) {
        return DecisionResponseByFilteredApprover.builder().proposalId(proposal.getId())
            .title(proposal.getTitle()).approvers(approvers).build();
    }
}
