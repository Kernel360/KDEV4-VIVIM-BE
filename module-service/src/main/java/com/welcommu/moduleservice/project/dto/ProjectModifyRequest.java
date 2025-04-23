package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
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
public class ProjectModifyRequest {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<ProjectUserRoleRequest> clientManagers;
    private List<ProjectUserRoleRequest> clientUsers;
    private List<ProjectUserRoleRequest> devManagers;
    private List<ProjectUserRoleRequest> devUsers;

    private List<Long> companyIds;


    public Project modifyProject(Project project) {
        project.setName(this.name);
        project.setDescription(this.description);
        project.setStartDate(this.startDate);
        project.setEndDate(this.endDate);
        project.setModifiedAt(LocalDateTime.now());
        return project;
    }

    public List<ProjectUser> toProjectUsers(Project project, Function<Long, User> userResolver) {
        List<ProjectUser> result = new java.util.ArrayList<>();

        for (ProjectUserRoleRequest dto : clientManagers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId()),
                ProjectUserManageRole.CLIENT_MANAGER));
        }
        for (ProjectUserRoleRequest dto : clientUsers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId()),
                ProjectUserManageRole.CLIENT_USER));
        }
        for (ProjectUserRoleRequest dto : devManagers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId()),
                ProjectUserManageRole.DEVELOPER_MANAGER));
        }
        for (ProjectUserRoleRequest dto : devUsers) {
            result.add(dto.toEntity(project, userResolver.apply(dto.getUserId()),
                ProjectUserManageRole.DEVELOPER_USER));
        }
        return result;
    }
}