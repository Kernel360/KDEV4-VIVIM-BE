package com.welcommu.moduleservice.approval.approvalProposal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalModifyRequest {

    private String title;
    private String content;
    private boolean isApproved;

//    private List<Long> approverIdList;

//    public List<ApprovalApprover> toApprovalApprovers(ApprovalProposal approvalProposal,
//        List<ProjectUser> approvers) {
//        return approvers.stream()
//            .map(user -> ApprovalApprover.builder()
//                .approvalProposal(approvalProposal)
//                .projectUser(user)
//                .isApproved(false)
//                .build())
//            .toList();
//    }
}
