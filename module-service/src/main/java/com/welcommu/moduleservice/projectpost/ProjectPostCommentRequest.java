package com.welcommu.moduleservice.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCommentRequest {
    private String comment;

    public ProjectPostComment toEntity(Long projectId, ProjectPostCommentRequest request) {
        return ProjectPostComment.builder()
                .comment(request.comment)
                .creatorId(1L) //테스트용으로 넣어뒀습니다.
                .projectId(projectId)
                .build();
    }
}
