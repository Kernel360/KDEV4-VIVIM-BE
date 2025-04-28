package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ProjectUserResponse {

    private Long userId;
    private String userName;
    private ProjectUserManageRole role;

    public static ProjectUserResponse from(ProjectUser pu) {
        return new ProjectUserResponse(
            pu.getUser().getId(),
            pu.getUser().getName(),
            pu.getProjectUserManageRole()
        );
    }
}
