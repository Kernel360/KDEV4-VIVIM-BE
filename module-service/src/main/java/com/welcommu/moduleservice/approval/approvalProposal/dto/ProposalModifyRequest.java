package com.welcommu.moduleservice.approval.approvalProposal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalModifyRequest {

    private String title;
    private String content;
    private boolean isApproved;
}
