package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectUserRoleRequest {
    private Long userId;
    private ProjectUserManageRole role;

    public ProjectUser toEntity(Project project, User user) {
        return ProjectUser.builder()
                .project(project)
                .user(user)
                .projectUserManageRole(role)
                .build();
    }
}

