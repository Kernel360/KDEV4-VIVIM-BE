package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
public class ProjectPostCommentDeleteRequest {
    public void deleteTo(ProjectPostComment originalComment) {
        originalComment.setDeletedAt(LocalDateTime.now());
    }
}
