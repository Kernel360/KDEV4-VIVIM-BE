package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
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