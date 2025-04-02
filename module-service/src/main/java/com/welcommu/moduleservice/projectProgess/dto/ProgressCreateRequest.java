package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProgressCreateRequest {

    @NotNull
    private String name;

    public ProjectProgress toEntity(Project project) {
        return ProjectProgress.builder()
            .name(this.name)
            .createdAt(LocalDateTime.now())
            .project(project)
            .build();
    }
}
