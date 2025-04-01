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
public class ProjectPostCommentRequest {
    private String comment;

    public ProjectPostComment toEntity(Long projectPostId, ProjectPostCommentRequest request, String clientIp) {
        return ProjectPostComment.builder()
                .comment(request.comment)
                .createdAt(LocalDateTime.now())
                .writerIp(clientIp)
                .creatorId(1L) //테스트 용
                .projectPostId(projectPostId)
                .build();
    }
}
