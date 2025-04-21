package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalResponseList {

    private List<ProposalResponse> approvalList;

    public static ProposalResponseList from(List<ApprovalProposal> approvalList) {

        List<ProposalResponse> approvalListResponse = approvalList.stream()
            .map(ProposalResponse::of) // approver 미포함
            .toList();

        return ProposalResponseList.builder()
            .approvalList(approvalListResponse)
            .build();
    }
}
