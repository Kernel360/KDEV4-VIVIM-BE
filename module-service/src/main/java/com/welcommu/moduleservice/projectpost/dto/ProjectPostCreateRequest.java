package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCreateRequest {
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;

    public ProjectPost toEntity(Long projectId, ProjectPostCreateRequest request) {
        return ProjectPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .projectPostStatus(request.getProjectPostStatus())
                .createdAt(LocalDateTime.now())
                .creatorId(1L)//1L는 테스트 용입니다
                .writerIp("102.176.0.0") //테스트 용입니다.
                .projectId(projectId)
                .build();
    }

}
