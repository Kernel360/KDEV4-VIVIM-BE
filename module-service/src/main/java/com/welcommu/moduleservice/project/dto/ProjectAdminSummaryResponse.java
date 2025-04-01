package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectAdminSummaryResponse {
    private Long projectId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static ProjectAdminSummaryResponse from(Project p) {
        return new ProjectAdminSummaryResponse(
                p.getId(),
                p.getName(),
                p.getStartDate(),
                p.getEndDate(),
                p.getIsDeleted(),
                p.getDeletedAt()
        );
    }
}
