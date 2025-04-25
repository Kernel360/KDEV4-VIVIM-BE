package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotBlank
    private String name;
    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private List<Long> companyIds;

    private List<ProjectUserListCreate> clientManagers;
    private List<ProjectUserListCreate> clientUsers;
    private List<ProjectUserListCreate> devManagers;
    private List<ProjectUserListCreate> devUsers;

    public Project toEntity() {
        return Project.builder()
            .name(name)
            .description(description)
            .startDate(startDate)
            .endDate(endDate)
            .createdAt(LocalDateTime.now())
            .isDeleted(false)
            .build();
    }

    // ProjectUser 목록으로 변환
    public List<ProjectUser> toProjectUsers(Project project, Function<Long, User> userResolver) {
        List<ProjectUser> result = new ArrayList<>();

        for (ProjectUserListCreate dto : clientManagers) {
            result.add(ProjectUser.builder()
                .project(project)
                .user(userResolver.apply(dto.getUserId()))
                .projectUserManageRole(ProjectUserManageRole.CLIENT_MANAGER)
                .build());
        }
        for (ProjectUserListCreate dto : clientUsers) {
            result.add(ProjectUser.builder()
                .project(project)
                .user(userResolver.apply(dto.getUserId()))
                .projectUserManageRole(ProjectUserManageRole.CLIENT_USER)
                .build());
        }
        for (ProjectUserListCreate dto : devManagers) {
            result.add(ProjectUser.builder()
                .project(project)
                .user(userResolver.apply(dto.getUserId()))
                .projectUserManageRole(ProjectUserManageRole.DEVELOPER_MANAGER)
                .build());
        }
        for (ProjectUserListCreate dto : devUsers) {
            result.add(ProjectUser.builder()
                .project(project)
                .user(userResolver.apply(dto.getUserId()))
                .projectUserManageRole(ProjectUserManageRole.DEVELOPER_USER)
                .build());
        }

        return result;
    }
}

