package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ProjectPostCommentDeleteRequest {

    public ProjectPostComment deleteTo(ProjectPostComment originalComment) {
        return originalComment.builder()
                .id(originalComment.getId())
                .comment(originalComment.getComment())
                .createdAt(originalComment.getCreatedAt())
                .modifiedAt(originalComment.getModifiedAt())
                .deletedAt(LocalDateTime.now())
                .isDeleted(true)
                .writerIp(originalComment.getWriterIp())
                .creatorId(originalComment.getCreatorId())
                .projectPostId(originalComment.getProjectPostId())
                .build();
    }
}
