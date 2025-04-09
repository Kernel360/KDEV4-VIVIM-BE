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
    private Long commentId;
    private String content;
    private Long parentId;
    private LocalDateTime createdAt;
    private String creatorName;
    private String creatorRole;
    private LocalDateTime modifiedAt;

    public static ProjectPostCommentListResponse from(ProjectPostComment comment) {
        return new ProjectPostCommentListResponse(
                comment.getId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getCreatedAt(),
                comment.getCreatorName(),
                comment.getCreatorRole(),
                comment.getModifiedAt()
        );
    }

}