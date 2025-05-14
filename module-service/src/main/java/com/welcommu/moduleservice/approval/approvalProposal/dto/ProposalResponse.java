package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduleservice.projectProgess.dto.ProgressResponse;
import com.welcommu.moduleservice.user.dto.UserResponse;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalResponse {

    private Long id;
    private String title;
    private String content;
    private ApprovalProposalStatus approvalProposalStatus;
    private LocalDateTime createdAt;
    private UserResponse creator;
    private ProgressResponse progress;
    private Long projectId;

    public static ProposalResponse of(ApprovalProposal approvalProposal) {
        return ProposalResponse.builder()
            .id(approvalProposal.getId())
            .title(approvalProposal.getTitle())
            .content(approvalProposal.getContent())
            .approvalProposalStatus(approvalProposal.getProposalStatus())
            .createdAt(approvalProposal.getCreatedAt())
            .creator(UserResponse.from(approvalProposal.getUser()))
            .progress(ProgressResponse.from(approvalProposal.getProjectProgress()))
            .projectId(approvalProposal.getProjectProgress().getProject().getId())
            .build();
    }
}
