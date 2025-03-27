package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProgressCreateRequest {

    @NotNull
    private String name;

    public ProjectProgress toEntity(User user, Project project) {
        return ProjectProgress.builder()
            .name(this.name)
            .user(user)
            .project(project)
            .build();
    }
}
