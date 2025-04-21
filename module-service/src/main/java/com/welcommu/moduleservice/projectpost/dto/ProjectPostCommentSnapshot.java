package com.welcommu.moduleservice.projectpost.dto;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostComment;
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
public class ProjectPostCommentSnapshot {
    private  Long id;
    private String content;
    private Long parentId;

    public static ProjectPostCommentSnapshot from(ProjectPostComment projectPostComment) {
        return new ProjectPostCommentSnapshot(
            projectPostComment.getId(),
            projectPostComment.getContent(),
            projectPostComment.getParentId()
        );
    }
}
