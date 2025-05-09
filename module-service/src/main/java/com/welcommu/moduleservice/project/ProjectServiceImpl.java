package com.welcommu.moduleservice.project;

import static com.welcommu.modulecommon.exception.CustomErrorCode.NOT_FOUND_COMPANY;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.projectCompany.ProjectCompany;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.notification.NotificationType;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectStatus;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.DefaultProjectProgress;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.company.CompanyRepository;
import com.welcommu.moduleinfra.project.ProjectCompanyRepository;
import com.welcommu.moduleinfra.project.ProjectRepository;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleinfra.user.UserRepository;
import com.welcommu.moduleservice.notification.NotificationService;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import com.welcommu.moduleservice.project.audit.ProjectAuditService;
import com.welcommu.moduleservice.project.dto.DashboardInspectionCountResponse;
import com.welcommu.moduleservice.project.dto.DashboardProgressCountResponse;
import com.welcommu.moduleservice.project.dto.DashboardProjectFeeResponse;
import com.welcommu.moduleservice.project.dto.ProjectAdminSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectCompanyResponse;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectModifyRequest;
import com.welcommu.moduleservice.project.dto.ProjectSnapshot;
import com.welcommu.moduleservice.project.dto.ProjectSummaryWithRoleDto;
import com.welcommu.moduleservice.project.dto.ProjectUserResponse;
import com.welcommu.moduleservice.project.dto.ProjectUserSummaryResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectAuditService projectAuditService;
    private final ProjectCompanyRepository projectCompanyRepository;
    private final CompanyRepository companyRepository;
    private final NotificationService notificationService;

    @Transactional
    public void createProject(ProjectCreateRequest dto, Long creatorId) {

        Project project = dto.toEntity();
        Project createProject = projectRepository.save(project);
        projectAuditService.createAuditLog(ProjectSnapshot.from(createProject), creatorId);

        initializeDefaultProgress(createProject);

        List<ProjectUser> participants = dto.toProjectUsers(project, userId ->
            userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER))
        );
        projectUserRepository.saveAll(participants);

        List<ProjectCompany> projectCompanies = dto.getCompanyIds().stream()
            .map(companyId -> {
                Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_COMPANY));
                return ProjectCompany.builder()
                    .project(createProject)
                    .company(company)
                    .build();
            }).toList();

        projectCompanyRepository.saveAll(projectCompanies);

        User creator = userRepository.findById(creatorId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));
        //참여자들에게 알림 전송
        for (ProjectUser participant : participants) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(participant.getUser().getId())
                .content(String.format("%s님이 '%s' 프로젝트를 생성했습니다.", creator.getName(), dto.getName()))
                .type(NotificationType.PROJECT_CREATED)
                .typeId(project.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

        //관리자에게 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(String.format("%s님이 '%s' 프로젝트를 생성했습니다.", creator.getName(), dto.getName()))
                .type(NotificationType.PROJECT_CREATED)
                .typeId(project.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

    }

    public Project getProject(User user, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        ;
        checkUserPermission(user, projectId);
        return project;

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

        projectCompanyRepository.deleteByProject(existingProject);
        projectCompanyRepository.flush();

        List<ProjectCompany> updatedCompanies = dto.getCompanyIds().stream()
            .map(companyId -> {
                Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMPANY));
                return ProjectCompany.builder()
                    .project(existingProject)
                    .company(company)
                    .build();
            }).toList();
        projectCompanyRepository.saveAll(updatedCompanies);

        User modifier = userRepository.findById(modifierId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));

        //참여자들에게 알림
        for (ProjectUser participant : updatedUsers) {
            Long userId = participant.getUser().getId();

            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(userId)
                .content(
                    String.format("%s님이 '%s'프로젝트를 수정했습니다.", modifier.getName(), dto.getName()))
                .type(NotificationType.PROJECT_MODIFIED)
                .typeId(projectId)
                .build();
            notificationService.sendNotification(notificationRequest);

        }

        //관리자에게 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(
                    String.format("%s님이 '%s'프로젝트를 수정했습니다.", modifier.getName(), dto.getName()))
                .type(NotificationType.PROJECT_MODIFIED)
                .typeId(existingProject.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

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

    @Transactional(readOnly = true)
    public Page<ProjectAdminSummaryResponse> searchProjects(
        String name,
        String description,
        Boolean isDeleted,
        Pageable pageable
    ) {
        Page<Project> projects = projectRepository.searchByConditions(name, description, isDeleted,
            pageable);
        return projects.map(ProjectAdminSummaryResponse::from);
    }

    @Transactional
    public void deleteProject(Long projectId, Long deleterId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        projectAuditService.deleteAuditLog(ProjectSnapshot.from(project), deleterId);
        ProjectDeleteRequest.deleteProject(project);

        List<ProjectUser> participants = projectUserRepository.findByProject(project);

        User deleter = userRepository.findById(deleterId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));

        //참여자들에게 알림 전송
        for (ProjectUser participant : participants) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(participant.getUser().getId())
                .content(
                    String.format("%s님이 '%s'프로젝트를 삭제했습니다.", deleter.getName(), project.getName()))
                .type(NotificationType.PROJECT_DELETED)
                .typeId(projectId)
                .build();
            notificationService.sendNotification(notificationRequest);
        }

        //관리자에게 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(
                    String.format("%s님이 '%s' 프로젝트를 삭제했습니다.", deleter.getName(), project.getName()))
                .type(NotificationType.PROJECT_DELETED)
                .typeId(project.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }
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

    public List<ProjectSummaryWithRoleDto> getCompanyProjectsWithMyRole(Long companyId,
        Long userId) {
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

    public List<ProjectCompanyResponse> getCompaniesByProjectId(Long projectId) {
        return projectCompanyRepository.findByProjectId(projectId).stream()
            .map(pc -> ProjectCompanyResponse.from(pc.getCompany()))
            .toList();
    }

    public DashboardProjectFeeResponse getDashboardProjectFee() {
        List<Project> projects = projectRepository.findAll();
        return new DashboardProjectFeeResponse(projects);
    }

    public DashboardInspectionCountResponse getDashboardInspectionCount() {
        List<Project> projects = projectRepository.findAll();
        return new DashboardInspectionCountResponse(projects);
    }

    public DashboardProgressCountResponse getDashboardProgressCount() {
        List<Project> projects = projectRepository.findAll();
        return new DashboardProgressCountResponse(projects);
    }

    @Transactional
    public void increaseCurrentProgress(Long projectId) {

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));

        DefaultProjectProgress currentProgress = project.getCurrentProgress();

        DefaultProjectProgress nextProgress = currentProgress.nextStep();

        project.setCurrentProgress(nextProgress);

        if (nextProgress == DefaultProjectProgress.INSPECTION) {
            project.setProjectStatus(ProjectStatus.INSPECTION);
        }
        if (nextProgress == DefaultProjectProgress.COMPLETED) {
            project.setProjectFeePaidDate();
            project.setProjectStatus(ProjectStatus.COMPLETED);
        }
    }
}
