package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCommentListResponse {
    private String comment;
    private LocalDateTime createdAt;

    public static ProjectPostCommentListResponse from(ProjectPostComment comment) {
        return new ProjectPostCommentListResponse(
                comment.getComment(),
                comment.getCreatedAt()
        );
    }

}