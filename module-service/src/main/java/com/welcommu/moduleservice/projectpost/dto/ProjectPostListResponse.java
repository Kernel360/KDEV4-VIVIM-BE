package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostListResponse {

    private Long postId;
    private String title;
    private String content;
    private Long creatorId;
    private ProjectPostStatus projectPostStatus;
    private LocalDateTime createdAt;
    private String creatorName;
    private CompanyRole creatorRole;
    private LocalDateTime modifiedAt;
    private Long parentId;
    private Long projectId;

    public static ProjectPostListResponse from(ProjectPost post) {
        return new ProjectPostListResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getCreator().getId(),
            post.getProjectPostStatus(),
            post.getCreatedAt(),
            post.getCreator().getName(),
            post.getCreator().getCompany().getCompanyRole(),
            post.getModifiedAt(),
            post.getParentId(),
            post.getProject().getId()
        );
    }

}