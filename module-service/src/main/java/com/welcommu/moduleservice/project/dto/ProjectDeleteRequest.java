package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class ProjectDeleteRequest {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public static void deleteProject(Project project){
        project.setIsDeleted(true);
        project.setDeletedAt(LocalDateTime.now());

    }
}