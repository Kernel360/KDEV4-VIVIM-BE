package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalStatus;
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

//    private List<Long> approverIdList;

    public ApprovalProposal toEntity(User creator, ProjectProgress progress) {
        return ApprovalProposal.builder()
            .title(this.title)
            .content(this.content)
            .approvalStatus(ApprovalStatus.APPROVAL_BEFORE_PROPOSAL)
            .createdAt(LocalDateTime.now())
            .user(creator)
            .projectProgress(progress)
            .build();
    }

    // TODO Approver 도메인으로 분리 예정
//    public List<ApprovalApprover> toApprovalApprovers(ApprovalProposal approvalProposal,
//        List<ProjectUser> approvers) {
//        return approvers.stream()
//            .map(user -> ApprovalApprover.builder()
//                .approvalProposal(approvalProposal)
//                .projectUser(user)
//                .build())
//            .toList();
//    }
}
