package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostListResponse {

    private Long postId;
    private String title;
    private String content;
    private Long creatorId;
    private ProjectPostStatus projectPostStatus;
    private LocalDateTime createdAt;
    private String creatorName;
    private String creatorRole;
    private LocalDateTime modifiedAt;
    private Long parentId;

    public static ProjectPostListResponse from(ProjectPost post) {
        return new ProjectPostListResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getCreatorId(),
            post.getProjectPostStatus(),
            post.getCreatedAt(),
            post.getCreatorName(),
            post.getCreatorRole(),
            post.getModifiedAt(),
            post.getParentId()
        );
    }

}