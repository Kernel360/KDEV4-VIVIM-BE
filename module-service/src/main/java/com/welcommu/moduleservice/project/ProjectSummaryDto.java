package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummaryDto {
    private Long projectId;
    private String projectName;
    private ProjectStatus status;
    private ProjectUserRole myRole;

    public static ProjectSummaryDto of(Project project, ProjectUser projectUser) {
        return new ProjectSummaryDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                projectUser.getProjectUserRole()
        );
    }
}