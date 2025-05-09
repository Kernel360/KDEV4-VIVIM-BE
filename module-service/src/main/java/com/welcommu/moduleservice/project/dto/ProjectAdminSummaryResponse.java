package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.projectprogress.DefaultProjectProgress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectAdminSummaryResponse {

    private Long projectId;
    private String name;
    private int projectFee;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private ProjectStatus projectStatus;
    private DefaultProjectProgress currentProgress;
    private LocalDate projectFeePaidDate;

    public static ProjectAdminSummaryResponse from(Project p) {
        return new ProjectAdminSummaryResponse(
            p.getId(),
            p.getName(),
            p.getProjectFee() != null ? p.getProjectFee() : 0,  // null → 0
            p.getStartDate(),
            p.getEndDate(),
            p.getIsDeleted(),
            p.getDeletedAt(),
            p.getProjectStatus(),
            p.getCurrentProgress(),
            p.getProjectFeePaidDate()
        );
    }
}
