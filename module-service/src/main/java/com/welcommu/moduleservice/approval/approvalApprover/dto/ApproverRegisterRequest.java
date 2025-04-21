package com.welcommu.moduleservice.approval.approvalApprover.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproverRegisterRequest {

    @NotEmpty
    private List<Long> projectUserIdList;

    public List<ApprovalApprover> toAllApprover(ApprovalProposal proposal,
        List<ProjectUser> projectUsers) {
        return projectUsers.stream()
            .map(user -> ApprovalApprover.builder()
                .approvalProposal(proposal)
                .projectUser(user)
                .build())
            .collect(Collectors.toList());
    }
}