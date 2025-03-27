package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Getter
@Builder
@AllArgsConstructor
public class ProjectUpdateRequest {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    // 참여자 정보
    private List<UserRoleDto> clientManagers;
    private List<UserRoleDto> clientUsers;
    private List<UserRoleDto> devManagers;
    private List<UserRoleDto> devUsers;


    public void updateProject(Project project) {
        project.setName(this.name);
        project.setDescription(this.description);
        project.setStartDate(this.startDate);
        project.setEndDate(this.endDate);
        project.setModifiedAt(LocalDateTime.now());
    }

    // 참여자 변환
    public List<com.welcommu.moduledomain.project.ProjectUser> toProjectUsers(Project project, Function<Long, User> userResolver) {
        List<com.welcommu.moduledomain.project.ProjectUser> result = new java.util.ArrayList<>();

        for (UserRoleDto dto : clientManagers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId())));
        }
        for (UserRoleDto dto : clientUsers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId())));
        }
        for (UserRoleDto dto : devManagers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId())));
        }
        for (UserRoleDto dto : devUsers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId())));
        }

        return result;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserRoleDto {
        private Long userId;
        private com.welcommu.moduledomain.project.ProjectUserManageRole role;

        public ProjectUser toEntity(Project project, User user) {
            return ProjectUser.builder()
                    .project(project)
                    .user(user)
                    .projectUserManageRole(role)
                    .build();
        }
    }
}