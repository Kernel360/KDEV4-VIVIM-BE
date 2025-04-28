package com.welcommu.moduleservice.approval.approvalDecision.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 하나의 승인요청(ApprovalProposal)에 대해, 해당 요청에 포함된 승인권자들과 그들의 승인응답 목록을 담은 응답
 */

@Getter
@Builder
public class DecisionResponsesByAllApprover {

    private Long proposalId;
    private List<DecisionResponsesByOneApprover> decisionResponses;

    public static DecisionResponsesByAllApprover from(Long proposalId, List<DecisionResponsesByOneApprover> decisionResponses) {

        return DecisionResponsesByAllApprover.builder()
            .proposalId(proposalId)
            .decisionResponses(decisionResponses)
            .build();
    }
}
