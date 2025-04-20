package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalSendResponse {

    private String message;

    public static ProposalSendResponse from(User user, ApprovalProposal proposal) {
        String companyName = user.getCompany().getName();
        String userName = user.getName();
        String proposalTitle = proposal.getTitle();

        String message = String.format(
            "[승인요청] %s 개발사 소속 %s님이 \"%s\" 요청을 보냈습니다.",
            companyName, userName, proposalTitle
        );

        return new ProposalSendResponse(message);
    }
}
