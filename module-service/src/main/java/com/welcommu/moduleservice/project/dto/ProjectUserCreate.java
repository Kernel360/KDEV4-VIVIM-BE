package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectUserCreate {
    private final Long userId;
    private final ProjectUserManageRole manageRole;

    public ProjectUser toEntity(Project project, User user) {
        return ProjectUser.builder()
                .project(project)
                .user(user)
                .projectUserManageRole(manageRole)
                .build();
    }
}
