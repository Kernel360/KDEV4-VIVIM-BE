package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;

import java.time.LocalDateTime;

public record ProjectPostListResult(String title, Long creatorId, ProjectPostStatus projectPostStatus, LocalDateTime createdAt) {
    public static ProjectPostListResult from(ProjectPost post) {
        return new ProjectPostListResult(post.getTitle(), post.getCreatorId(), post.getProjectPostStatus(), post.getCreatedAt());
    }
}
