package com.welcommu.moduleservice.approval.approvalApprover.dto;


import com.welcommu.moduledomain.approval.ApprovalApprover;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApproverResponseList {

    private List<ApproverResponse> approverResponses;

    public static ApproverResponseList from(List<ApprovalApprover> approvalApprovers) {
        List<ApproverResponse> approverResponses = approvalApprovers.stream().map(
            approver -> ApproverResponse.builder().approverId(approver.getId())
                .userId(approver.getProjectUser().getUser().getId())
                .name(approver.getProjectUser().getUser().getName()).decisionStatus("PENDING")
                .build()).collect(Collectors.toList());

        return new ApproverResponseList(approverResponses);
    }
}
