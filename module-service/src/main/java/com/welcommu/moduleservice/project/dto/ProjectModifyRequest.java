package com.welcommu.moduleservice.project.dto;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectUser.ProjectUserManageRole;
import com.welcommu.moduledomain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectModifyRequest {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer projectFee;

    private List<ProjectUserRoleRequest> clientManagers;
    private List<ProjectUserRoleRequest> clientUsers;
    private List<ProjectUserRoleRequest> devManagers;
    private List<ProjectUserRoleRequest> devUsers;

    private List<Long> companyIds;


    public Project modifyProject(Project project) {
        if (this.name != null) project.setName(this.name);
        if (this.description != null) project.setDescription(this.description);
        if (this.startDate != null) project.setStartDate(this.startDate);
        if (this.endDate != null) project.setEndDate(this.endDate);
        if (this.projectFee != null) project.setProjectFee(this.projectFee);
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