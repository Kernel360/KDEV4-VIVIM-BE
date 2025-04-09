package com.welcommu.moduleservice.checklist.dto;

import com.welcommu.moduledomain.checklist.Checklist;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChecklistCreateRequest {

    @NotBlank
    private String name;

    public Checklist toEntity(ProjectProgress progress, String name) {
        return Checklist.builder()
            .name(name)
            .createdAt(LocalDateTime.now())
            .projectProgress(progress)
            .build();
    }
}