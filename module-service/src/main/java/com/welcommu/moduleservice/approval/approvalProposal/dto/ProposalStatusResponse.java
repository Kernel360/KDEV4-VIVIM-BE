package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProposalStatusResponse {

    private int totalApproverCount;                // 총 승인권자 수
    private int approvedApproverCount;             // 승인 완료한 승인권자 수
    private int modificationRequestedApproverCount; // 수정 요청한 승인권자 수
    private int waitingApproverCount;               // 응답 대기중인 승인권자 수
    private ApprovalProposalStatus proposalStatus; // 승인요청 전체 상태

    public static ProposalStatusResponse of(
        int totalApproverCount,
        int approvedApproverCount,
        int modificationRequestedApproverCount,
        int waitingApproverCount,
        ApprovalProposalStatus proposalStatus
    ) {

        return ProposalStatusResponse.builder()
            .totalApproverCount(totalApproverCount)
            .approvedApproverCount(approvedApproverCount)
            .modificationRequestedApproverCount(modificationRequestedApproverCount)
            .waitingApproverCount(waitingApproverCount)
            .proposalStatus(proposalStatus)
            .build();
    }
}
