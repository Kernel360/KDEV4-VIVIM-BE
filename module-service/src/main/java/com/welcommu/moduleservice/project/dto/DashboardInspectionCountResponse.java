package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DashboardInspectionCountResponse {

    private int inspectionCount;
    private int progressCount;

    public DashboardInspectionCountResponse(List<Project> projects) {
        for (Project project : projects) {
            if (project.getProjectStatus() == ProjectStatus.PROGRESS) {
                progressCount++;
            } else if (project.getProjectStatus() == ProjectStatus.INSPECTION) {
                inspectionCount++;
            }
        }
    }
}
