package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserRole;
import com.welcommu.moduledomain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectUserRequestDto {
    private Long userId;
    private ProjectUserRole role;

    public ProjectUser toEntity(Project project, User user){
        return ProjectUser.builder()
                .project(project)
                .user(user)
                .projectUserRole(role)
                .build();
    }
}
