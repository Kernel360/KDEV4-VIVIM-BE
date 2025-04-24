package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

/**
 * 하나의 승인권자(Approver)가 수행한 모든 승인응답(ApprovalDecision)
 */

@Getter
@Builder
public class DecisionResponsesByOneApprover {

    private Long userId;
    private Long approverId;
    private String approverName;
    private List<DecisionResponse> decisionResponses;

    public static DecisionResponsesByOneApprover from(ApprovalApprover approver,
        List<ApprovalDecision> decisions) {
        return DecisionResponsesByOneApprover.builder()
            .approverId(approver.getId())
            .userId(approver.getProjectUser()
                .getUser()
                .getId())
            .approverName(approver.getProjectUser()
                .getUser()
                .getName())
            .decisionResponses(decisions.stream()
                .map(DecisionResponse::from)
                .collect(Collectors.toList()))
            .build();
    }
}
