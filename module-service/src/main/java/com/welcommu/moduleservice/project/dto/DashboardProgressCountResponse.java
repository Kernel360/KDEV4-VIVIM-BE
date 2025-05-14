package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
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
            String progress = project.getCurrentProgress();
            if (progress != null) {
                switch (progress) {
                    case "요구사항 정의":
                        requirementCount++;
                        break;
                    case "화면설계":
                        wireframeCount++;
                        break;
                    case "디자인":
                        designCount++;
                        break;
                    case "배포":
                        publishCount++;
                        break;
                    case "개발":
                        developCount++;
                        break;
                    case "검수":
                        inspectionCount++;
                        break;
                }
            }
        }
    }
}
