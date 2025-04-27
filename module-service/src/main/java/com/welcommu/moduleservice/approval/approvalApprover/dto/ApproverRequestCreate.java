package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalApproverStatus;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApproverRequestCreate {
    
    private List<Long> approverIds;
    
    public ApprovalApprover toEntity(ApprovalProposal proposal, ProjectUser projectUser) {
        return ApprovalApprover.builder()
            .approvalProposal(proposal)
            .projectUser(projectUser)
            .approverStatus(ApprovalApproverStatus.BEFORE_REQUEST)
            .build();
    }
}
