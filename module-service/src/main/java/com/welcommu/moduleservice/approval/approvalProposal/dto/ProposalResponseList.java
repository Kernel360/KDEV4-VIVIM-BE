package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalResponseList {

    private List<ProposalResponse> approvalListResponse;

    public static ProposalResponseList from(
        List<ApprovalProposal> approvalProposalList,
        Map<Long, List<ApprovalApprover>> approverMap
    ) {
        List<ProposalResponse> approvalListResponse = approvalProposalList.stream()
            .map(approval -> ProposalResponse.of(
                approval,
                approverMap.getOrDefault(approval.getId(), List.of())
            ))
            .toList();

        return ProposalResponseList.builder()
            .approvalListResponse(approvalListResponse)
            .build();
    }
}
