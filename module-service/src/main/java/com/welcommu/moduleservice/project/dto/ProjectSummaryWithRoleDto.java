package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import io.micrometer.common.lang.Nullable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummaryWithRoleDto {
    private Long projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String myRole;  // null 이면 내가 참여하지 않은 프로젝트

    public static ProjectSummaryWithRoleDto from(Project project, @Nullable ProjectUser projectUser) {
        return new ProjectSummaryWithRoleDto(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            projectUser != null ? projectUser.getProjectUserManageRole().name() : null
        );
    }
}
