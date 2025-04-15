package com.welcommu.moduleservice.approve.dto;

import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ApprovalCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public Approval toEntity(Checklist checklist) {
        return Approval.builder()
            .title(this.title)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .checklist(checklist)
            .build();
    }
}
