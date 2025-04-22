package com.welcommu.moduleservice.project;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.projectprogress.DefaultProjectProgress;
import com.welcommu.moduleservice.project.audit.ProjectAuditService;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.ProjectSnapshot;
import com.welcommu.moduleservice.project.dto.ProjectAdminSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectModifyRequest;
import com.welcommu.moduleservice.project.dto.ProjectSummaryWithRoleDto;
import com.welcommu.moduleservice.project.dto.ProjectUserResponse;
import com.welcommu.moduleservice.project.dto.ProjectUserSummaryResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectAuditService projectAuditService;

    @Transactional
    public void createProject(ProjectCreateRequest dto, Long creatorId) {

        Project project = dto.toEntity();
        Project createProject = projectRepository.save(project);
        projectAuditService.createAuditLog(ProjectSnapshot.from(createProject) , creatorId);

        initializeDefaultProgress(createProject);

        List<ProjectUser> participants = dto.toProjectUsers(project, userId ->
            userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER))
        );
        projectUserRepository.saveAll(participants);
    }

    public Optional<Project> getProject(User user, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));;
        checkUserPermission(user, projectId);
        return projectRepository.findById(projectId);

    }

    @Transactional
    public void modifyProject(Long projectId, ProjectModifyRequest dto, Long modifierId) {
        Project existingProject = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));

        // auditLog 기록을 위해 수정 전 데이터로 구성된 객체를 생성
        ProjectSnapshot beforeSnapshot = ProjectSnapshot.from(existingProject);

        dto.modifyProject(existingProject);

        // auditLog 기록을 위해 수정 후 데이터로 구성된 객체를 생성
        ProjectSnapshot afterSnapshot = ProjectSnapshot.from(existingProject);

        // auditLog 기록을 위해 수정 전, 후 객체를 바탕으로 audit_log 기록
        projectAuditService.modifyAuditLog(beforeSnapshot, afterSnapshot, modifierId);

        projectUserRepository.deleteByProject(existingProject);
        projectUserRepository.flush();
        List<ProjectUser> updatedUsers = dto.toProjectUsers(existingProject, id ->
            userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER))
        );
        projectUserRepository.saveAll(updatedUsers);
    }


    public List<ProjectUserSummaryResponse> getProjectsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));

        List<ProjectUser> projectUsers = projectUserRepository.findByUser(user);

        return projectUsers.stream()
            .filter(pu -> !pu.getProject().getIsDeleted())
            .map(pu -> ProjectUserSummaryResponse.of(pu.getProject(), pu))
            .collect(Collectors.toList());
    }

    public List<ProjectAdminSummaryResponse> getProjectList() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
            .map(ProjectAdminSummaryResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProject(Long projectId, Long deleterId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        projectAuditService.deleteAuditLog(ProjectSnapshot.from(project), deleterId);
        ProjectDeleteRequest.deleteProject(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectUserResponse> getUserListByProject(Long projectId) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(projectId);
        List<ProjectUser> projectUsers = projectUserRepository.findByProject(project);

        return projectUsers.stream()
            .map(ProjectUserResponse::from)
            .collect(Collectors.toList());
    }

    public List<ProjectAdminSummaryResponse> getCompanyProjects(Long companyId) {
        return projectRepository.findAllByCompanyId(companyId).stream()
            .map(ProjectAdminSummaryResponse::from)
            .toList();
    }

    public List<ProjectSummaryWithRoleDto> getCompanyProjectsWithMyRole(Long companyId, Long userId) {
        List<Project> projects = projectRepository.findAllByCompanyId(companyId);

        Map<Long, ProjectUser> myRoles = projectUserRepository.findByUserId(userId).stream()
            .collect(Collectors.toMap(pu -> pu.getProject().getId(), Function.identity()));

        return projects.stream()
            .map(project -> ProjectSummaryWithRoleDto.from(project, myRoles.get(project.getId())))
            .toList();
    }

    private void initializeDefaultProgress(Project project) {

        float position = 1.0f;
        for (DefaultProjectProgress defaultProjectProgress : DefaultProjectProgress.values()) {
            ProjectProgress progress = ProjectProgress.builder()
                    .name(defaultProjectProgress.getLabel())
                    .position(position)
                    .createdAt(LocalDateTime.now())
                    .project(project)
                    .build();
            progressRepository.save(progress);
            position += 1.0f;
        }
    }

    private void checkUserPermission(User user, Long projectId) {
        if (projectUserRepository.findByUserIdAndProjectId(user.getId(), projectId).isEmpty() && !(
            user.getCompany().getCompanyRole() == CompanyRole.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER);
        }
    }
}
