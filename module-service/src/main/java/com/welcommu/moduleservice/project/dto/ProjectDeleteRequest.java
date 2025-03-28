package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;

import java.time.LocalDateTime;

public class ProjectDeleteRequest {

    public static void deleteProject(Project project){
        project.setIsDeleted(true);
        project.setDeletedAt(LocalDateTime.now());
    }
}