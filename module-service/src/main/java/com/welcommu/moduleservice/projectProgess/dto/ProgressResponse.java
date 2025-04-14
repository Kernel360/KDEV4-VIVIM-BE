package com.welcommu.moduleservice.projectProgess.dto;

import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {

    private Long id;
    private String name;
    private Float position;

    public static ProgressResponse of(ProjectProgress progress) {
        return new ProgressResponse(progress.getId(), progress.getName(), progress.getPosition());
    }
}