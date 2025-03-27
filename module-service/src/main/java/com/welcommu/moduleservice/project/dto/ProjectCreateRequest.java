package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.project.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
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

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<UserIdDto> clientManagers;
    private List<UserIdDto> clientUsers;
    private List<UserIdDto> devManagers;
    private List<UserIdDto> devUsers;

    @Getter
    @AllArgsConstructor
    public static class UserIdDto {
        private Long userId;
    }

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

        for (UserIdDto dto : clientManagers) {
            result.add(ProjectUser.builder()
                    .project(project)
                    .user(userResolver.apply(dto.getUserId()))
                    .projectUserManageRole(ProjectUserManageRole.CLIENT_MANAGER)
                    .build());
        }
        for (UserIdDto dto : clientUsers) {
            result.add(ProjectUser.builder()
                    .project(project)
                    .user(userResolver.apply(dto.getUserId()))
                    .projectUserManageRole(ProjectUserManageRole.CLIENT_USER)
                    .build());
        }
        for (UserIdDto dto : devManagers) {
            result.add(ProjectUser.builder()
                    .project(project)
                    .user(userResolver.apply(dto.getUserId()))
                    .projectUserManageRole(ProjectUserManageRole.DEVELOPER_MANAGER)
                    .build());
        }
        for (UserIdDto dto : devUsers) {
            result.add(ProjectUser.builder()
                    .project(project)
                    .user(userResolver.apply(dto.getUserId()))
                    .projectUserManageRole(ProjectUserManageRole.DEVELOPER_USER)
                    .build());
        }

        return result;
    }
}

