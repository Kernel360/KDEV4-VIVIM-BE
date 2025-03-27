package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostListResponse {
    private String title;
    private Long creatorId;
    private ProjectPostStatus projectPostStatus;
    private LocalDateTime createdAt;

    public static ProjectPostListResponse from(ProjectPost post) {
        return new ProjectPostListResponse(
                post.getTitle(),
                post.getCreatorId(),
                post.getProjectPostStatus(),
                post.getCreatedAt()
        );
    }

}