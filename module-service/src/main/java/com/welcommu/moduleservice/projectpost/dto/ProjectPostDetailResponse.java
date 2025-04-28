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
public class ProjectPostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private Long parentId;
    private ProjectPostStatus projectPostStatus;
    private Long creatorId;
    private LocalDateTime createdAt;
    private String creatorName;
    private CompanyRole creatorRole;
    private LocalDateTime modifiedAt;
    private String responseToQuestion;


    public static ProjectPostDetailResponse from(ProjectPost post) {
        return new ProjectPostDetailResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getParentId(),
            post.getProjectPostStatus(),
            post.getCreator().getId(),
            post.getCreatedAt(),
            post.getCreator().getName(),
            post.getCreator().getCompany().getCompanyRole(),
            post.getModifiedAt(),
            post.getResponseToQuestion()
        );
    }
}
