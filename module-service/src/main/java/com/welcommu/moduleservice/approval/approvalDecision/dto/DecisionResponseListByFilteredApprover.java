package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 하나의 승인요청(ApprovalProposal)에 대해, 해당 요청에 포함된 승인권자들과 그들의 승인응답 목록을 담은 응답
 */

@Getter
@Builder
public class DecisionResponseListByFilteredApprover {

    private Long proposalId;
    private String title;
    private List<DecisionResponseListByApprover> approvers;

    public static DecisionResponseListByFilteredApprover from(ApprovalProposal proposal,
        List<DecisionResponseListByApprover> approvers) {
        return DecisionResponseListByFilteredApprover.builder().proposalId(proposal.getId())
            .title(proposal.getTitle()).approvers(approvers).build();
    }
}
