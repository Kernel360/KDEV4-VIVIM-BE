package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;
    private Long  parentId;

    public ProjectPost toEntity(Long projectId, ProjectPostRequest request, String clientIp) {
        return ProjectPost.builder()
                .projectId(projectId)
                .title(request.getTitle())
                .content(request.getContent())
                .projectPostStatus(request.getProjectPostStatus())
                .createdAt(LocalDateTime.now())
                .creatorId(1L)//1L는 테스트 용입니다.
                .writerIp(clientIp)
                .parentId(request.parentId)
                .build();
    }
}
