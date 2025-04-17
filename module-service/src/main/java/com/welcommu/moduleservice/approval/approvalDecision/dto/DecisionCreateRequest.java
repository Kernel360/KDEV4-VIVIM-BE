package com.welcommu.moduleservice.approval.approvalDecision.dto;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalStatus;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
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
            .writer(creator)
            .approvalStatus(this.approvalStatus)
            .build();
    }
}
