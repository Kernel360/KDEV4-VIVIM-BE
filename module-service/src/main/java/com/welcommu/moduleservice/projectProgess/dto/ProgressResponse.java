package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProgressResponse {

    private Long id;
    private String name;
    private float position;

    public static ProgressResponse of(ProjectProgress progress) {
        return new ProgressResponse(progress.getId(), progress.getName(), progress.getPosition());
    }
}