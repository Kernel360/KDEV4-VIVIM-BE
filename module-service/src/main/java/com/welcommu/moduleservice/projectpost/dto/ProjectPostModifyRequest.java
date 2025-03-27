package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostModifyRequest {
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;

    public void updateProjectPost(ProjectPost originalPost, ProjectPostModifyRequest request) {
        originalPost.setTitle(request.getTitle());
        originalPost.setContent(request.getContent());
        originalPost.setProjectPostStatus(request.getProjectPostStatus());
        originalPost.setModifiedAt(LocalDateTime.now());
        originalPost.setModifierId(1L);//테스트용
    }
}
