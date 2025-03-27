package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.moduledomain.projectpost.enums.ProjectPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ProjectPostDeleteRequest {

    public void deleteTo(ProjectPost originalPost) {

        originalPost.setDeleted(true);
        originalPost.setDeletedAt(LocalDateTime.now());
        originalPost.setDeleterId(1L);//테스트용

    }
}
