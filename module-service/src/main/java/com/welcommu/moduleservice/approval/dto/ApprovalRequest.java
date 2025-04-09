package com.welcommu.moduleservice.approval.dto;

import com.welcommu.moduledomain.approval.Approval;
import com.welcommu.moduledomain.checklist.Checklist;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ApprovalRequest {

    private String title;
    private String content;

    public Approval toEntity(Checklist checklist) {
        return Approval.builder()
            .checklist(checklist)
            .title(this.title)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
