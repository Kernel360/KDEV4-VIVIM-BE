package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectPostSnapshot {
    private Long id;
    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;
    private Long parentId;

    public static ProjectPostSnapshot from(ProjectPost projectPost) {
        return new ProjectPostSnapshot(
            projectPost.getId(),
            projectPost.getTitle(),
            projectPost.getContent(),
            projectPost.getProjectPostStatus(),
            projectPost.getParentId()
        );
    }
}
