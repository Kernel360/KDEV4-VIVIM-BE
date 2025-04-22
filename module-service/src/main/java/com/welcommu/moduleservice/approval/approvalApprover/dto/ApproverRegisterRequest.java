package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproverRegisterRequest {

    private List<Long> approverUserIds;

    public ApprovalApprover toEntity(ApprovalProposal proposal, ProjectUser projectUser) {
        return ApprovalApprover.builder()
            .approvalProposal(proposal)
            .projectUser(projectUser)
            .build();
    }
}
