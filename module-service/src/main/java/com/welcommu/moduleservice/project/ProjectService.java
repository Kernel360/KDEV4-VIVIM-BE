package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.project.dto.DashboardInspectionCountResponse;
import com.welcommu.moduleservice.project.dto.DashboardProgressCountResponse;
import com.welcommu.moduleservice.project.dto.DashboardProjectFeeResponse;
import com.welcommu.moduleservice.project.dto.ProjectAdminSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectCompanyResponse;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectModifyRequest;
import com.welcommu.moduleservice.project.dto.ProjectSummaryWithRoleDto;
import com.welcommu.moduleservice.project.dto.ProjectUserResponse;
import com.welcommu.moduleservice.project.dto.ProjectUserSummaryResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    void createProject(ProjectCreateRequest dto, Long creatorId);

    Project getProject(User user, Long projectId);

    void modifyProject(Long projectId, ProjectModifyRequest dto, Long modifierId);

    List<ProjectUserSummaryResponse> getProjectsByUser(Long userId);

    List<ProjectAdminSummaryResponse> getProjectList();

    Page<ProjectAdminSummaryResponse> searchProjects(String name, String description,
        Boolean isDeleted, Pageable pageable);

    void deleteProject(Long projectId, Long deleterId);

    List<ProjectUserResponse> getUserListByProject(Long projectId);

    List<ProjectAdminSummaryResponse> getCompanyProjects(Long companyId);

    List<ProjectSummaryWithRoleDto> getCompanyProjectsWithMyRole(Long companyId, Long userId);

    List<ProjectCompanyResponse> getCompaniesByProjectId(Long projectId);

    DashboardProjectFeeResponse getDashboardProjectFee();

    DashboardInspectionCountResponse getDashboardInspectionCount();

    DashboardProgressCountResponse getDashboardProgressCount();

    void increaseCurrentProgress(Long projectId);

}
