package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostDetailResponse {
    private String title;
    private String description;

    public static ProjectPostDetailResponse from(ProjectPost post) {
        return new ProjectPostDetailResponse(
            post.getTitle(),
            post.getContent()
        );
    }
}
