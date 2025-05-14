package com.welcommu.moduleservice.projectProgess;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.approval.ApprovalProposalRepository;
import com.welcommu.moduleinfra.project.ProjectRepository;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.projectProgess.dto.ProgressApprovalStatusResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressCreateRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressListResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressNameModifyRequest;
import com.welcommu.moduleservice.projectProgess.dto.ProgressApprovalStatusOverallResponse;
import com.welcommu.moduleservice.projectProgess.dto.ProgressPositionModifyRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository projectRepository;
    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ApprovalProposalRepository proposalRepository;

    @Transactional
    public void createProgress(User user, Long projectId, ProgressCreateRequest request) {
        Project project = findProject(projectId);
        checkUserPermission(user, projectId);
        checkIsDuplicatedProgressName(projectId, request.getName());

        Float biggestPosition = findBiggestPosition(projectId);

        ProjectProgress projectProgress = request.toEntity(project);
        projectProgress.setPosition(biggestPosition + 1.0f);

        progressRepository.save(projectProgress);
    }

    @Transactional
    public void modifyProgressName(User user, Long projectId, Long progressId,
        ProgressNameModifyRequest request) {

        findProject(projectId);
        findProgress(progressId);

        checkUserPermission(user, projectId);
        checkIsDuplicatedProgressName(projectId, request.getName());

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);
        projectProgress.setName(request.getName());
        progressRepository.save(projectProgress);
    }

    @Transactional
    public void modifyProgressPosition(User user, Long projectId, Long progressId,
        ProgressPositionModifyRequest request) {

        findProject(projectId);
        findProgress(progressId);
        checkUserPermission(user, projectId);

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);

        Integer targetIndex = request.getTargetIndex();
        if (targetIndex == null || targetIndex < 0) {
            throw new CustomException(CustomErrorCode.MISSING_TARGET_INDEX);
        }

        List<ProjectProgress> progresses = progressRepository.findByProjectIdOrderByPosition(projectId);

        float newPosition;
        if (progresses.isEmpty()) {
            newPosition = 0.0f;
        } else if (targetIndex == 0) {
            newPosition = progresses.get(0).getPosition() / 2.0f;
        } else if (targetIndex >= progresses.size()) {
            newPosition = progresses.get(progresses.size() - 1).getPosition() + 1.0f;
        } else {
            float prevPosition = progresses.get(targetIndex - 1).getPosition();
            float nextPosition = progresses.get(targetIndex).getPosition();
            newPosition = (prevPosition + nextPosition) / 2.0f;
        }

        projectProgress.setPosition(newPosition);
        progressRepository.save(projectProgress);
    }

    public void deleteProgress(User user, Long projectId, Long progressId) {

        findProject(projectId);
        checkUserPermission(user, projectId);

        ProjectProgress projectProgress = checkIsMatchedProject(projectId, progressId);
        progressRepository.delete(projectProgress);
    }

    // 각 단계별 totalApprovalCount, approvedApprovalCount, progressRate를 조회해서 progressList로 주는 역할
    public ProgressApprovalStatusResponse getProgressApprovalStatus(Long projectId) {
        Project project = findProject(projectId);
        List<ProjectProgress> progressList = progressRepository.findByProject(project);

        List<ProgressApprovalStatusResponse.ProgressApprovalStatus> progressApprovalStatuses =
            progressList.stream()
                .map(progress -> {
                    Long totalCount = proposalRepository.countByProjectProgressId(progress.getId());
                    Long approvedCount = proposalRepository.countByProjectProgressIdAndProposalStatus(
                        progress.getId(), ApprovalProposalStatus.FINAL_APPROVED
                    );
                    float progressRate = (totalCount == 0) ? 0.0f : (approvedCount * 1.0f / totalCount) * 100;

                    return new ProgressApprovalStatusResponse.ProgressApprovalStatus(
                        progress.getId(),
                        progress.getName(),
                        totalCount,
                        approvedCount,
                        progressRate
                    );
                })
                .toList();

        return new ProgressApprovalStatusResponse(progressApprovalStatuses);
    }

    public ProgressListResponse getProgressList(Long projectId) {
        Project project = findProject(projectId);
        List<ProjectProgress> progressList = progressRepository.findByProject(project);

        return ProgressListResponse.of(progressList);
    }

    // 완료된 프로젝트 진행 단계 계산
    @Transactional
    public ProgressApprovalStatusOverallResponse calculateOverallProgress(Long projectId) {
        ProgressApprovalStatusResponse progressStatus = getProgressApprovalStatus(projectId);
        List<ProgressApprovalStatusResponse.ProgressApprovalStatus> progresses = progressStatus.getProgressList();

        int totalStageCount = progresses.size();
        int completedStageCount = 0;
        float currentStageProgressRate = 0.0f;

        for (ProgressApprovalStatusResponse.ProgressApprovalStatus progress : progresses) {
            if (progress.getProgressRate() == 1.0f) {
                completedStageCount++;
            } else {
                currentStageProgressRate = progress.getProgressRate();
                break;
            }
        }

        float overallProgressRate = ((completedStageCount + currentStageProgressRate) / totalStageCount) * 100.0f;

        return ProgressApprovalStatusOverallResponse.builder()
            .totalStageCount(totalStageCount)
            .completedStageCount(completedStageCount)
            .currentStageProgressRate(currentStageProgressRate)
            .overallProgressRate(overallProgressRate)
            .build();
    }

    // 진행단계별 완료된 승인요청 비율
    public int calculateFinalApprovedRate(Long progressId) {
        ProjectProgress projectProgress = progressRepository.findById(progressId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ProjectProgress ID: " + progressId));

        List<ApprovalProposal> proposals = proposalRepository.findByProjectProgress(projectProgress);

        if (proposals.isEmpty()) {
            return 0;
        }

        long totalCount = proposals.size();
        long finalApprovedCount = proposals.stream()
            .filter(proposal -> proposal.getProposalStatus() == ApprovalProposalStatus.FINAL_APPROVED)
            .count();

        return (int) ((finalApprovedCount * 100) / totalCount);
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