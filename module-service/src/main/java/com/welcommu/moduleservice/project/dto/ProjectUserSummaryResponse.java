package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectUserSummaryResponse {
    private Long projectId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private ProjectUserManageRole myRole;

    public static ProjectUserSummaryResponse of(Project project, ProjectUser projectUser) {
        return new ProjectUserSummaryResponse(
                project.getId(),
                project.getName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getIsDeleted(),
                project.getDeletedAt(),
                projectUser.getProjectUserManageRole()
        );
    }
}