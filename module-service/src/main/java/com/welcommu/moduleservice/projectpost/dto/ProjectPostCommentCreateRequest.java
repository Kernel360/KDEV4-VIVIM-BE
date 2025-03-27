package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCommentCreateRequest {
    private String comment;

    public ProjectPostComment toEntity(Long projectPostId, ProjectPostCommentCreateRequest request) {
        return ProjectPostComment.builder()
                .comment(request.comment)
                .createdAt(LocalDateTime.now())
                .writerIp("102.176.0.0") //테스트 용
                .creatorId(1L) //테스트 용
                .projectPostId(projectPostId)
                .build();
    }
}
