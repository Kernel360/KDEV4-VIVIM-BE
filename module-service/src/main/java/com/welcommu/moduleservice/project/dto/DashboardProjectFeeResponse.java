package com.welcommu.moduleservice.project.dto;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.welcommu.moduledomain.project.Project;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
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
        LocalDate currentDate = LocalDate.now();

        // 1. 이번 달에 속하는 프로젝트만 필터링
        Map<Integer, Integer> weekFeeMap = projects.stream()
            .filter(p -> p.getProjectFeePaidDate() != null
                && p.getProjectFeePaidDate().getYear() == currentDate.getYear()
                && p.getProjectFeePaidDate().getMonth() == currentDate.getMonth())
            .collect(groupingBy(
                p -> p.getProjectFeePaidDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH),
                summingInt(Project::getProjectFee)
            ));

        // 2. 결과 매핑
        this.week1 = weekFeeMap.getOrDefault(1, 0);
        this.week2 = weekFeeMap.getOrDefault(2, 0);
        this.week3 = weekFeeMap.getOrDefault(3, 0);
        this.week4 = weekFeeMap.getOrDefault(4, 0);
        this.week5 = weekFeeMap.getOrDefault(5, 0);

    }
}
