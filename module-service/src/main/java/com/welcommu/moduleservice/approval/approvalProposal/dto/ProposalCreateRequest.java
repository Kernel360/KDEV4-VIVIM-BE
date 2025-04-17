package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<Long> approverIdList;

    public ApprovalProposal toEntity(User creator, ProjectProgress progress) {
        return ApprovalProposal.builder()
            .title(this.title)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .writer(creator)
            .progress(progress)
            .build();
    }

    public List<ApprovalApprover> toApprovalApprovers(ApprovalProposal approvalProposal,
        List<ProjectUser> approvers) {
        return approvers.stream()
            .map(user -> ApprovalApprover.builder()
                .approvalProposal(approvalProposal)
                .projectUser(user)
                .build())
            .toList();
    }
}
