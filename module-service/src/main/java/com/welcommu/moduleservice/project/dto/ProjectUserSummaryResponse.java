package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectUserSummaryResponse {
    private Long projectId;
    private String projectName;
    private ProjectUserManageRole myRole;

    public static ProjectUserSummaryResponse of(Project project, ProjectUser projectUser) {
        return new ProjectUserSummaryResponse(
                project.getId(),
                project.getName(),
                projectUser.getProjectUserManageRole() // ✅ 역할 정보 포함
        );
    }
}