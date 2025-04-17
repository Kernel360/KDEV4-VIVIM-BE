package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import com.welcommu.moduledomain.user.User;
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
    private Long parentId;

    public ProjectPost toEntity(User user, Long projectId, ProjectPostRequest request,
        String clientIp) {
        return ProjectPost.builder()
            .projectId(projectId)
            .title(request.getTitle())
            .content(request.getContent())
            .projectPostStatus(request.getProjectPostStatus())
            .createdAt(LocalDateTime.now())
            .creatorId(user.getId())
            .creatorName(user.getName())
            .creatorRole(user.getCompany().getCompanyRole().toString())
            .writerIp(clientIp)
            .parentId(request.parentId)
            .build();
    }
}
