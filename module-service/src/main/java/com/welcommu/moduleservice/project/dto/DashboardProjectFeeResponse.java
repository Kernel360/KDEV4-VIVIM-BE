package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardProjectFeeResponse {

    private int week1;
    private int week2;
    private int week3;
    private int week4;
    private int week5;


    public DashboardProjectFeeResponse(List<Project> projects) {
        // 이번 달의 첫 날과 마지막 날을 구함
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        week1 = 0;
        week2 = 0;
        week3 = 0;
        week4 = 0;
        week5 = 0;

        for (Project project : projects) {
            LocalDate projectFeeDate = project.getProjectFeePaidDate();
            Integer projectFee = project.getProjectFee();

            if (projectFeeDate != null && projectFeeDate.getYear() == currentDate.getYear()
                && projectFeeDate.getMonth() == currentDate.getMonth()) {
                // 프로젝트의 정산일이 이번 달에 속하는 경우만 처리
                int weekOfMonth = projectFeeDate.get(
                    ChronoField.ALIGNED_WEEK_OF_MONTH);
                projectFeeDate.getDayOfWeek().getValue();

                switch (weekOfMonth) {
                    case 1:
                        week1 += projectFee;
                        break;
                    case 2:
                        week2 += projectFee;
                        break;
                    case 3:
                        week3 += projectFee;
                        break;
                    case 4:
                        week4 += projectFee;
                        break;
                    case 5:
                        week5 += projectFee;
                        break;
                    default:
                        break;
                }
            }
        }


    }
}
