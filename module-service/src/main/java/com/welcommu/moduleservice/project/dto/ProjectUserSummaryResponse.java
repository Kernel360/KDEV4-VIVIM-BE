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

    public static ProjectUserSummaryResponse of(Project p, ProjectUser pu) {
        return new ProjectUserSummaryResponse(
                p.getId(),
                p.getName(),
                p.getStartDate(),
                p.getEndDate(),
                p.getIsDeleted(),
                p.getDeletedAt(),
                pu.getProjectUserManageRole()
        );
    }
}