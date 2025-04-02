package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummaryResponse {
    private Long projectId;
    private String projectName;
    private ProjectUserManageRole myRole;

    public static ProjectSummaryResponse of(Project project, ProjectUser projectUser) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getName(),
                projectUser.getProjectUserManageRole() // ✅ 역할 정보 포함
        );
    }
}