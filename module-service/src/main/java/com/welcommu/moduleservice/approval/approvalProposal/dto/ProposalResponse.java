package com.welcommu.moduleservice.approval.approvalProposal.dto;

import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import java.time.LocalDateTime;
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
//    private List<ApproverResponse> approverList;

    public static ProposalResponse of(ApprovalProposal approvalProposal) {
        return ProposalResponse.builder()
            .id(approvalProposal.getId())
            .title(approvalProposal.getTitle())
            .content(approvalProposal.getContent())
            .approvalStatus(approvalProposal.getApprovalStatus())
            .createdAt(approvalProposal.getCreatedAt())
            .creator(approvalProposal.getUser())
            .progress(approvalProposal.getProjectProgress())
//            .approverList(null) // 아직 미구현
            .build();
    }
}
