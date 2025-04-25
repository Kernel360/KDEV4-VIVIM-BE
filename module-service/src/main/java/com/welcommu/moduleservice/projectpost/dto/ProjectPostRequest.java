package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import com.welcommu.moduledomain.user.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {

    private String title;
    private String content;
    private ProjectPostStatus projectPostStatus;
    private Long parentId;

    public ProjectPost toEntity(User user, Project project, ProjectPostRequest request,
        String clientIp) {
        return ProjectPost.builder()
            .project(project)
            .title(request.getTitle())
            .content(request.getContent())
            .projectPostStatus(request.getProjectPostStatus())
            .createdAt(LocalDateTime.now())
            .creator(user)
            .writerIp(clientIp)
            .parentId(request.parentId)
            .build();
    }
}
