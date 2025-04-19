package com.welcommu.moduleservice.approval.approvalDecision.dto;

import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalStatus;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DecisionCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ApprovalStatus approvalStatus;

    public ApprovalDecision toEntity(User creator) {
        return ApprovalDecision.builder()
            .title(this.title)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .user(creator)
            .approvalStatus(this.approvalStatus)
            .build();
    }
}
