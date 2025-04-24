package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecisionResponseSend {

    private String message;

    public static DecisionResponseSend from(User user, ApprovalDecision decision) {
        String companyName = user.getCompany()
            .getName();
        String userName = user.getName();

        String statusMessage = switch (decision.getDecisionStatus()) {
            case APPROVED -> "승인";
            case REJECTED -> "수정요청 및 반려";
            default -> decision.getDecisionStatus()
                .toString();
        };

        String message = String.format("[승인응답] %s 고객사 소속 %s 님으로부터 \"%s\" 응답이 돌아왔습니다.", companyName, userName, statusMessage);

        return new DecisionResponseSend(message);
    }
}