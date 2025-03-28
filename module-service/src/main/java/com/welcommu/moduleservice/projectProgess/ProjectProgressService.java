package com.welcommu.moduleservice.projectProgess;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;

    public void createProgress(
        User creator,
        Long projectId,
        ProgressCreateRequest request
    ) {

        Project project = findProject(projectId);
        float biggestPosition = findBiggestPosition(projectId);

        ProjectProgress projectProgress = request.toEntity(creator, project);
        projectProgress.setPosition(biggestPosition + 1);  // position 값을 기존 최대값 +1로 설정

        progressRepository.save(projectProgress);
    }

    public void updateProgress(Long projectId, Long progressId, ProgressCreateRequest request) {

        ProjectProgress projectProgress = returnIfIsMatchedProject(projectId, progressId);
        projectProgress.setName(request.getName());
        progressRepository.save(projectProgress);
    }

    public void deleteProgress(Long projectId, Long progressId) {

        ProjectProgress projectProgress = returnIfIsMatchedProject(projectId, progressId);
        progressRepository.delete(projectProgress);
    }

    private ProjectProgress returnIfIsMatchedProject(Long projectId, Long progressId) {
        Project project = findProject(projectId);
        ProjectProgress projectProgress = findProgress(progressId);

        if (!projectProgress.getProject().getName().equals(project.getName()) && !projectProgress.getProject().getCreatedAt().equals(project.getCreatedAt())) {
            throw new CustomException(CustomErrorCode.MISMATCH_PROJECT_PROGRESS);
        }
        return projectProgress;
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
        return progressRepository.findMaxPositionByProjectId(projectId);  // 프로젝트에 단계가 없다면 기본값 0
    }
}