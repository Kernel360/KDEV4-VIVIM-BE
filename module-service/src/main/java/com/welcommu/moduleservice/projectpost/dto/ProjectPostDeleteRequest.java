package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostDeleteRequest {

    public ProjectPost deleteTo(ProjectPost originalPost) {
        return originalPost.builder()
                .id(originalPost.getId())
                .title(originalPost.getTitle())
                .content(originalPost.getContent())
                .projectPostStatus(originalPost.getProjectPostStatus())
                .creatorId(originalPost.getCreatorId())
                .createdAt(originalPost.getCreatedAt())
                .modifiedAt(originalPost.getModifiedAt())
                .deletedAt(LocalDateTime.now())
                .isDeleted(true)
                .modifierId(originalPost.getModifierId())
                .deleterId(1L)//1L는 테스트 용입니다.
                .writerIp(originalPost.getWriterIp())
                .projectId(originalPost.getProjectId())
                .build();
    }
}
