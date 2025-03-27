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
public class ProjectPostModifyRequest {
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;

    public ProjectPost toEntity(ProjectPost originalPost, ProjectPostModifyRequest request) {
        return originalPost.builder()
                .id(originalPost.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .projectPostStatus(request.getProjectPostStatus())
                .creatorId(1L)//1L는 테스트 용입니다.
                .createdAt(originalPost.getCreatedAt())
                .modifiedAt(LocalDateTime.now())
                .modifierId(1L)//1L는 테스트 용입니다.
                .writerIp(originalPost.getWriterIp())
                .projectId(originalPost.getProjectId())
                .build();
    }
}
