package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    private Long id;
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;

    public ProjectPost toEntity(Long projectId, ProjectPostRequest request) {
        return ProjectPost.builder()
                .projectId(projectId)
                .title(request.getTitle())
                .content(request.getContent())
                .projectPostStatus(request.getProjectPostStatus())
                .creatorId(1L)//1L는 테스트 용입니다.
                .build();
    }
}
