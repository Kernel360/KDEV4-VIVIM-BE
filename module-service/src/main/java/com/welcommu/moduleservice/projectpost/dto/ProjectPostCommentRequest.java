package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.moduledomain.user.User;
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
    private String content;
    private Long parentId;

    public ProjectPostComment toEntity(User user, Long projectPostId, ProjectPostCommentRequest request, String clientIp) {
        return ProjectPostComment.builder()
                .content(request.content)
                .createdAt(LocalDateTime.now())
                .writerIp(clientIp)
                .creatorId(user.getId())
                .creatorName(user.getName())
                .creatorRole(user.getCompany().getCompanyRole().toString())
                .projectPostId(projectPostId)
                .parentId(request.parentId)
                .build();
    }
}
