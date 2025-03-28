package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostCommentModifyRequest {
    private String comment;

    public void modifyProjectPostComment(ProjectPostComment originalComment, ProjectPostCommentModifyRequest request) {
        originalComment.setComment(request.comment);

    }
}
