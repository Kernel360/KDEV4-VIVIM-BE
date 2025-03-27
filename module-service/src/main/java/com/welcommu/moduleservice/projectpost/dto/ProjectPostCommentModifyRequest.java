package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCommentModifyRequest {
    private String comment;

    public void updateProjectPostComment(ProjectPostComment originalComment, ProjectPostCommentModifyRequest request) {
        originalComment.setComment(request.comment);

    }
}
