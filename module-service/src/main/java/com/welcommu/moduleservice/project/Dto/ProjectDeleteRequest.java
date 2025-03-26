package com.welcommu.moduleservice.project.Dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.project.ProjectUserRole;
import com.welcommu.moduledomain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProjectDeleteRequest {

    private String name;
    private String description;
    private ProjectStatus projectStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long modifierId; // 수정자 ID

    private List<ProjectUserRequest> users; // 수정 후의 참여자 목록

    @Getter
    @Setter
    public static class UserRoleDto {
        private Long userId;
        private ProjectUserRole role;
    }

    public Project deleteTo(Project project, User deleter) {
        return project.toBuilder()
                .id(project.getId()) // 기존 ID 유지
                .isDeleted(true)
                .deletedAt(LocalDateTime.now())
                .deleter(deleter)
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}