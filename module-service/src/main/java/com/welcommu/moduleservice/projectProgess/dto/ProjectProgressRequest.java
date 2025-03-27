package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProjectProgressRequest {

    @NotNull
    private String name;
    private int order;
    private LocalDateTime createdAt;
    private User creator;
    private Project project;

    public ProjectProgress toEntity(User user, Project project) {
        return ProjectProgress.builder()
            .name(this.name)
            .order(this.order)
            .createdAt(this.createdAt)
            .creator(user)
            .project(project)
            .build();
    }
}
