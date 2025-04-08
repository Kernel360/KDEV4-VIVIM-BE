package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostDetailResponse {
    private Long postId;
    private String title;
    private String description;
    private Long parentId;

    public static ProjectPostDetailResponse from(ProjectPost post) {
        return new ProjectPostDetailResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getParentId()
        );
    }
}
