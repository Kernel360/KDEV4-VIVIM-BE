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

    private Long week1;
    private Long week2;
    private Long week3;
    private Long week4;
    private Long week5;


    public DashboardProjectFeeResponse(List<Project> projects) {
        // 이번 달의 첫 날과 마지막 날을 구함
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        week1 = 0L;
        week2 = 0L;
        week3 = 0L;
        week4 = 0L;
        week5 = 0L;

        for (Project project : projects) {
            LocalDate projectFeeDate = project.getProjectFeePaidDate();
            Long projectFee = project.getProjectFee();

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
