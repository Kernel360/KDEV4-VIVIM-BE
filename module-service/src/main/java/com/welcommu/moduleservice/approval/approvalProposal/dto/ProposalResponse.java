package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.approval.approvalApprover.dto.ApproverResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalResponse {

    private Long id;
    private String title;
    private String content;
    private ApprovalStatus approvalStatus;
    private LocalDateTime createdAt;
    private User creator;
    private ProjectProgress progress;
    private List<ApproverResponse> approverList;

    public static ProposalResponse of(ApprovalProposal approvalProposal,
        List<ApprovalApprover> approverList) {
        return ProposalResponse.builder()
            .id(approvalProposal.getId())
            .title(approvalProposal.getTitle())
            .content(approvalProposal.getContent())
            .approvalStatus(ApprovalStatus.APPROVAL_BEFORE_PROPOSAL)
            .createdAt(approvalProposal.getCreatedAt())
            .creator(approvalProposal.getUser())
            .progress(approvalProposal.getProgress())
            .approverList(
                approverList.stream()
                    .map(a -> ApproverResponse.of(a.getProjectUser(), a.isApproved()))
                    .toList()
            )
            .build();
    }
}
