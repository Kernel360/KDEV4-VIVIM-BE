package com.welcommu.moduleservice.projectProgess;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.project.ProjectRepository;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressModifyRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;

    public void createProgress(User user, Long projectId, ProgressCreateRequest request) {
        Project project = findProject(projectId);
        checkUserPermission(user, projectId);
        checkIsDuplicatedProgressName(projectId, request.getName());

        Float biggestPosition = findBiggestPosition(projectId);

        ProjectProgress projectProgress = request.toEntity(project);
        projectProgress.setPosition(biggestPosition + 1.0f);

        progressRepository.save(projectProgress);
    }

    public void modifyProgress(User user, Long projectId, Long progressId,
        ProgressModifyRequest request) {

        findProject(projectId);
        findProgress(progressId);

        checkUserPermission(user, projectId);
        checkIsDuplicatedProgressName(projectId, request.getName());
        //        if (request.getPosition() != null) {
        //            checkIsDuplicatedPosition(projectId, request.getPosition());
        //        }

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);
        projectProgress.setName(request.getName());
        //        if (request.getPosition() != null) {
        //            projectProgress.setPosition(request.getPosition());
        //        }
        progressRepository.save(projectProgress);
    }

    public void deleteProgress(User user, Long projectId, Long progressId) {

        findProject(projectId);
        checkUserPermission(user, projectId);

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

        if (!projectProgress.getProject()
            .getName()
            .equals(project.getName()) || !projectProgress.getProject()
            .getCreatedAt()
            .equals(project.getCreatedAt())) {
            throw new CustomException(CustomErrorCode.MISMATCH_PROJECT_PROGRESS);
        }
        return projectProgress;
    }

    private void checkIsDuplicatedProgressName(Long projectId, String name) {
        if (progressRepository.existsByProjectIdAndName(projectId, name)) {
            throw new CustomException(CustomErrorCode.DUPLICATE_PROGRESS_NAME);
        }
    }

    private void checkIsDuplicatedPosition(Long projectId, Float position) {
        if (progressRepository.existsByProjectIdAndPosition(projectId, position)) {
            throw new CustomException(CustomErrorCode.DUPLICATE_PROGRESS_POSITION);
        }
    }

    private void checkUserPermission(User user, Long projectId) {
        if (isAdmin(user)) {
            return;
        }
        ProjectUser projectUser = findProjectUser(user, projectId);
        if (!isDeveloperManager(user, projectUser)) {
            throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_DEVELOPER);
        }
    }

    private boolean isAdmin(User user) {
        return user.getRole()
            .toString()
            .equals("ADMIN");
    }

    private boolean isDeveloperManager(User user, ProjectUser projectUser) {

        boolean isDevManager = "DEVELOPER_MANAGER".equals(projectUser.getProjectUserManageRole()
            .toString());

        if (user.getCompany() == null || !isDevManager) {
            throw new CustomException(CustomErrorCode.FORBIDDEN_ACCESS);
        } else {
            return true;
        }
    }

    private ProjectUser findProjectUser(User user, Long projectId) {
        return projectUserRepository.findByUserIdAndProjectId(user.getId(), projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    }

    private ProjectProgress findProgress(Long progressId) {
        return progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }

    private Float findBiggestPosition(Long projectId) {
        return progressRepository.findMaxPositionByProjectId(projectId)
            .orElse(0.0f);
    }
}