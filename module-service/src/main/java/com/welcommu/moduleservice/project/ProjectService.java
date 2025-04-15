package com.welcommu.moduleservice.project;

import com.welcommu.moduleservice.logging.ProjectAuditService;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.ProjectAdminSummaryResponse;
import com.welcommu.moduleservice.project.dto.ProjectCreateRequest;
import com.welcommu.moduleservice.project.dto.ProjectDeleteRequest;
import com.welcommu.moduleservice.project.dto.ProjectModifyRequest;
import com.welcommu.moduleservice.project.dto.ProjectUserResponse;
import com.welcommu.moduleservice.project.dto.ProjectUserSummaryResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        projectAuditService.logCreateAudit(createProject, creatorId);

        initializeDefaultProgress(createProject);

        List<ProjectUser> participants = dto.toProjectUsers(project, userId ->
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId))
        );
        projectUserRepository.saveAll(participants);
    }

    public Optional<Project> getProject(Long projectId){
        return projectRepository.findById(projectId);

    }

    @Transactional
    public void modifyProject(Long projectId, ProjectModifyRequest dto, Long modifierId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        Project beforeModifyProject = project.snapshot();
        Project afterModifyProject = dto.modifyProject(project);
        projectAuditService.logUpdateAudit(beforeModifyProject, afterModifyProject, modifierId);

        projectUserRepository.deleteByProject(afterModifyProject);
        projectUserRepository.flush();
        List<ProjectUser> updatedUsers = dto.toProjectUsers(project, id ->
                userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음: ID = " + id))
        );
        projectUserRepository.saveAll(updatedUsers);
    }


    public List<ProjectUserSummaryResponse> getProjectsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));
        projectAuditService.logDeleteAudit(project, deleterId);
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

    private void initializeDefaultProgress(Project project) {
        List<String> defaultSteps = Arrays.asList(
            "요구사항 정의", "화면설계", "디자인", "퍼블리싱", "개발", "검수"
        );
        // 시작 순서를 1.0부터 부여
        float position = 1.0f;
        for (String step : defaultSteps) {
            ProjectProgress progress = ProjectProgress.builder()
                .name(step)
                .position(position)
                .createdAt(LocalDateTime.now())
                .project(project)
                .build();
            progressRepository.save(progress);
            position += 1.0f;
        }
    }
}
