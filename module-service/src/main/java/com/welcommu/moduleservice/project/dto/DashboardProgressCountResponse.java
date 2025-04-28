package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.DefaultProjectProgress;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DashboardProgressCountResponse {

    private int requirementCount = 0;
    private int wireframeCount = 0;
    private int designCount = 0;
    private int publishCount = 0;
    private int developCount = 0;
    private int inspectionCount = 0;

    public DashboardProgressCountResponse(List<Project> projects) {
        for (Project project : projects) {
            DefaultProjectProgress progress = project.getCurrentProgress();
            if (progress != null) {
                switch (progress) {
                    case REQUIREMENTS:
                        requirementCount++;
                        break;
                    case WIREFRAME:
                        wireframeCount++;
                        break;
                    case DESIGN:
                        designCount++;
                        break;
                    case PUBLISHING:
                        publishCount++;
                        break;
                    case DEVELOPMENT:
                        developCount++;
                        break;
                    case INSPECTION:
                        inspectionCount++;
                        break;
                }
            }
        }
    }
}
