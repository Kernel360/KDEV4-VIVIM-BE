package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    @Transactional
    public void createProject(ProjectCreateRequest dto) {
        Project project = dto.toEntity();
        projectRepository.save(project);

        List<ProjectUser> participants = dto.toProjectUsers(project, userId ->
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId))
        );
        projectUserRepository.saveAll(participants);
    }

    public Project getProject(Long projectId){
        return projectRepository.findByIdAndIsDeletedFalse(projectId);

    }

    @Transactional
    public void modifyProject(Long projectId, ProjectModifyRequest dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));
        dto.modifyProject(project);
        projectUserRepository.deleteByProject(project);
        List<ProjectUser> updatedUsers = dto.toProjectUsers(project, userId ->
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음: ID = " + userId))
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
    public void deleteProject(Long projectId) {
        Project deleted = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        ProjectDeleteRequest.deleteProject(deleted);
    }

    @Transactional(readOnly = true)
    public List<ProjectUserListResponse> getUserListByProject(Long projectId) {
        Project project = projectRepository.findByIdAndIsDeletedFalse(projectId);

        List<ProjectUser> projectUsers = projectUserRepository.findByProject(project);

        return projectUsers.stream()
                .map(ProjectUserListResponse::from)
                .collect(Collectors.toList());
    }

}
