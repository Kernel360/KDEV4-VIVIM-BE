package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectPostDeleteRequest {

    public void deleteTo(ProjectPost originalPost) {

        originalPost.setDeleted(true);
        originalPost.setDeletedAt(LocalDateTime.now());
        originalPost.setDeleterId(1L);//테스트용

    }
}
