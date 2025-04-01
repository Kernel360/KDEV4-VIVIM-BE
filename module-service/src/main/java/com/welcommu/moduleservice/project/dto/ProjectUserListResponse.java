package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ProjectUserListResponse {
    private Long userId;
    private String userName;
    private ProjectUserManageRole role;

    public static ProjectUserListResponse from(ProjectUser pu) {
        return new ProjectUserListResponse(
                pu.getUser().getId(),
                pu.getUser().getName(),
                pu.getProjectUserManageRole()
        );
    }
}
