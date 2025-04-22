package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

/**
 * 각 승인권자와 그들의 승인응답
 */

@Getter
@Builder
public class DecisionResponseByApproverList {
    
    private Long userId;
    private Long approverId;
    private String approverName;
    private List<DecisionResponse> decisionList;
    
    public static DecisionResponseByApproverList from(ApprovalApprover approver,
        List<ApprovalDecision> decisions) {
        return DecisionResponseByApproverList.builder().approverId(approver.getId())
            .userId(approver.getProjectUser().getUser().getId())
            .approverName(approver.getProjectUser().getUser().getName()).decisionList(
                decisions.stream().map(DecisionResponse::from).collect(Collectors.toList()))
            .build();
    }
}
