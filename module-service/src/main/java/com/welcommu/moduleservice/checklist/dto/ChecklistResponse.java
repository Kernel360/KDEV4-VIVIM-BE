package com.welcommu.moduleservice.checklist.dto;

import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChecklistResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private ProjectProgress progress;

    public static ChecklistResponse of(Checklist checklist) {
        return ChecklistResponse.builder()
            .id(checklist.getId())
            .name(checklist.getName())
            .createdAt(checklist.getCreatedAt())
            .progress(checklist.getProjectProgress())
            .build();
    }
}