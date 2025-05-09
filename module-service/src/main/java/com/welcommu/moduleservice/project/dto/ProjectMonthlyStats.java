package com.welcommu.moduleservice.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectMonthlyStats {
    private String month;
    private Long totalProjects;
    private Long completedProjects;
}
