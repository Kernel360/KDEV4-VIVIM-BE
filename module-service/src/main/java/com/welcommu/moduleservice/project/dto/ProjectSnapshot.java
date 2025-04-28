package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ProjectSnapshot {

    private final Long id;
    private final String name;
    private final String description;
    private final Integer projectFee;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ProjectSnapshot(Long id, String name, String description, Integer projectFee,
        LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectFee = projectFee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ProjectSnapshot from(Project project) {
        return new ProjectSnapshot(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getProjectFee(),
            project.getStartDate(),
            project.getEndDate()
        );
    }
}
