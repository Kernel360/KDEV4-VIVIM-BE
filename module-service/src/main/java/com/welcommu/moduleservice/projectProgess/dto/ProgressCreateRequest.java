package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressCreateRequest {

    @NotNull
    private String name;

    public ProjectProgress toEntity(Project project) {
        return ProjectProgress.builder()
            .name(this.name)
            .project(project)
            .build();
    }
}
