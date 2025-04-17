package com.welcommu.moduleservice.approval.approvalProposal;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalCreateRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalModifyRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponseList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProposalService {

    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalApproverRepository approverRepository;

    public void createApproval(User creator, Long progressId, ProposalCreateRequest request) {
        ProjectProgress progress = findProgress(progressId);
        checkUserPermission(creator, progress.getProject().getId());

        ApprovalProposal approvalProposal = request.toEntity(creator, progress);
        approvalProposalRepository.save(approvalProposal);

        List<ProjectUser> approvers = findApproverListById(request.getApproverIdList());
        List<ApprovalApprover> approvalApprovers = request.toApprovalApprovers(approvalProposal,
            approvers);

        approverRepository.saveAll(approvalApprovers);
    }

    public void modifyApproval(User user, Long progressId, Long approvalId,
        ProposalModifyRequest request) {
        ProjectProgress progress = findProgress(progressId);
        checkUserPermission(user, progress.getProject().getId());

        ApprovalProposal approvalProposal = findApproval(approvalId);

        if (request.getTitle() != null) {
            approvalProposal.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            approvalProposal.setContent(request.getContent());
        }
        approvalProposalRepository.save(approvalProposal);
    }

    public void deleteApproval(User user, Long approvalId) {

        ApprovalProposal approvalProposal = findApproval(approvalId);
        approvalProposalRepository.delete(approvalProposal);
    }

    public ProposalResponse getApproval(Long approvalId) {
        ApprovalProposal approvalProposal = findApproval(approvalId);
        List<ApprovalApprover> approvers = approverRepository.findByApprovalProposal(
            approvalProposal);

        return ProposalResponse.of(approvalProposal, approvers);
    }

    public ProposalResponseList getApprovalList(Long progressId) {
        ProjectProgress progress = findProgress(progressId);
        List<ApprovalProposal> approvalProposalList = approvalProposalRepository.findByProgress(
            progress);

        List<ApprovalApprover> approvers = approverRepository.findByApprovalProposalIn(
            approvalProposalList);

        Map<Long, List<ApprovalApprover>> approverMap = approvers.stream()
            .collect(Collectors.groupingBy(a -> a.getApprovalProposal().getId()));

        return ProposalResponseList.from(approvalProposalList, approverMap);
    }

    // TODO ApprovalDecision 저장 시 Approval 상태 갱신 흐름

    private void checkUserPermission(User user, Long projectId) {
        if (isAdmin(user)) return;
        if (isDeveloper(user)) {
            findProjectUser(user, projectId);
        } else {
            throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_DEVELOPER);
        }
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().toString().equals("ADMIN");
    }

    private boolean isDeveloper(User user) {
        return user.getRole() != null && user.getRole().toString().equals("DEVELOPER");
    }

    private void findProjectUser(User user, Long projectId) {
        projectUserRepository
            .findByUserIdAndProjectId(user.getId(), projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }

    private ProjectProgress findProgress(Long progressId) {
        return progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }

    private ApprovalProposal findApproval(Long approvalId) {
        return approvalProposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL));
    }

    private List<ProjectUser> findApproverListById(List<Long> approverId) {
        return projectUserRepository.findAllById(approverId);
    }
}
