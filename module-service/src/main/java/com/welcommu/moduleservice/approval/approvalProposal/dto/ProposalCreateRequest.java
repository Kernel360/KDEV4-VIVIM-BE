package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
    
    public ApprovalProposal toEntity(User creator, ProjectProgress progress) {
        return ApprovalProposal.builder()
            .title(this.title)
            .content(this.content)
            .proposalStatus(ApprovalProposalStatus.BEFORE_REQUEST_PROPOSAL)
            .createdAt(LocalDateTime.now())
            .user(creator)
            .projectProgress(progress)
            .build();
    }
}
