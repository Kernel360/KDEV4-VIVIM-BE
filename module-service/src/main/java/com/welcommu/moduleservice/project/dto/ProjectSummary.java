package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummary {
    private Long projectId;
    private String projectName;
    private ProjectUserManageRole myRole;

    public static ProjectSummary of(Project project, ProjectUser projectUser) {
        return new ProjectSummary(
                project.getId(),
                project.getName(),
                projectUser.getProjectUserManageRole() // ✅ 역할 정보 포함
        );
    }
}