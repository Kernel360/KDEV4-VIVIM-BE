package com.welcommu.moduleservice.project;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.project.ProjectUser;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.user.UserRepository;
import com.welcommu.moduleservice.project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 프로젝트 생성
        Project project = dto.toEntity();
        projectRepository.save(project);

        // 참여자 등록
        List<ProjectUser> participants = dto.toProjectUsers(project, userId ->
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId))
        );
        projectUserRepository.saveAll(participants);
    }

    @Transactional
    public void updateProject(Long projectId, ProjectUpdateRequest dto) {
        // 1. 기존 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        // 2. 직접 dto 업데이트
        dto.updateProject(project);

        // 3. 기존 참여자 제거
        projectUserRepository.deleteByProject(project);

        // 4. 참여자 재등록
        List<ProjectUser> updatedUsers = dto.toProjectUsers(project, userId ->
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음: ID = " + userId))
        );

        projectUserRepository.saveAll(updatedUsers);
    }

    public List<ProjectSummaryResponse> readProjectsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<ProjectUser> projectUsers = projectUserRepository.findByUser(user);

        return projectUsers.stream()
                .filter(pu -> !pu.getProject().getIsDeleted()) // 삭제되지 않은 프로젝트만 필터링
                .map(pu -> ProjectSummaryResponse.of(pu.getProject(), pu))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project deleted = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트 없음"));

        ProjectDeleteRequest.deleteProject(deleted);
    }
}
