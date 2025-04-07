package com.welcommu.moduleservice.projectProgess;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressModifyRequest;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;

    public void createProgress(
        User user,
        Long projectId,
        ProgressCreateRequest request
    ) {

        Project project = findProject(projectId);
        checkUserPermission(user, project);

        float biggestPosition = findBiggestPosition(projectId);

        ProjectProgress projectProgress = request.toEntity(project);
        projectProgress.setPosition(biggestPosition + 1.0f);

        progressRepository.save(projectProgress);
    }

    public void modifyProgress(User user, Long projectId, Long progressId, ProgressModifyRequest request) {

        Project project = findProject(projectId);
        findProgress(progressId);
        checkUserPermission(user, project);

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);
        projectProgress.setName(request.getName());
        projectProgress.setPosition(request.getPosition());
        progressRepository.save(projectProgress);
    }

    public void deleteProgress(User user, Long projectId, Long progressId) {

        Project project = findProject(projectId);
        checkUserPermission(user, project);

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);
        progressRepository.delete(projectProgress);
    }

    public ProgressListResponse getProgressList(Long projectId) {
        Project project = findProject(projectId);
        List<ProjectProgress> progressList = progressRepository.findByProject(project);

        return ProgressListResponse.of(progressList);
    }

    private ProjectProgress checkIsMatchedProject(Long projectId, Long progressId) {

        Project project = findProject(projectId);
        ProjectProgress projectProgress = findProgress(progressId);

        if (!projectProgress.getProject().getName().equals(project.getName()) || !projectProgress.getProject().getCreatedAt().equals(project.getCreatedAt())) {
            throw new CustomException(CustomErrorCode.MISMATCH_PROJECT_PROGRESS);
        }
        return projectProgress;
    }

    private void checkUserPermission(User user, Project project) {
        ProjectUser projectUser = projectUserRepository
            .findByUserIdAndProjectId(user.getId(), project.getId()).orElseThrow(()-> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));;
        if (user.getCompany() == null || !Objects.equals(user.getRole().toString(), "ADMIN") || !Objects.equals(projectUser.getProjectUserManageRole().toString(), "DEVELOPER_MANAGER")) {
            throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    }

    private ProjectProgress findProgress(Long progressId) {
        return progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }

    private float findBiggestPosition(Long projectId) {
        return progressRepository.findMaxPositionByProjectId(projectId).orElse(0.0f);
    }
}