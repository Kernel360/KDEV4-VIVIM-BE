package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.ProjectStatus;
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
}