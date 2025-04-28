package com.welcommu.moduleservice.approval.approvalProposal;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalApproverStatus;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalDecisionStatus;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.approval.ApprovalApproverRepository;
import com.welcommu.modulerepository.approval.ApprovalDecisionRepository;
import com.welcommu.modulerepository.approval.ApprovalProposalRepository;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalCreateRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalModifyRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponseList;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalSendResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalStatusResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProposalService {

    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ApprovalApproverRepository approvalApproverRepository;

    @Transactional
    public Long createProposal(User creator, Long progressId, ProposalCreateRequest request) {
        ProjectProgress progress = findProgress(progressId);
        checkUserPermission(creator, progress.getProject()
            .getId());

        ApprovalProposal approvalProposal = request.toEntity(creator, progress);
        approvalProposalRepository.save(approvalProposal);
        return approvalProposal.getId();
    }

    @Transactional
    public void modifyProposal(User user, Long approvalId, ProposalModifyRequest request) {

        ApprovalProposal approvalProposal = findProposal(approvalId);
        ProjectProgress progress = findProgress(approvalProposal.getProjectProgress()
            .getId());
        checkUserPermission(user, progress.getProject()
            .getId());

        if (request.getTitle() != null) {
            approvalProposal.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            approvalProposal.setContent(request.getContent());
        }
        approvalProposalRepository.save(approvalProposal);
    }

    @Transactional
    public void deleteProposal(Long approvalId) {

        ApprovalProposal approvalProposal = findProposal(approvalId);
        approvalProposalRepository.delete(approvalProposal);
    }

    @Transactional
    public ProposalSendResponse sendProposal(User user, Long approvalId) {

        ApprovalProposal proposal = findProposal(approvalId);
        checkUserPermission(user, proposal.getProjectProgress().getProject().getId());

        boolean hasApprovers = approvalApproverRepository.existsByApprovalProposal(proposal);
        if (!hasApprovers) {
            throw new CustomException(CustomErrorCode.NO_APPROVER_ASSIGNED);
        }

        proposal.markProposalSent();
        approvalProposalRepository.save(proposal);  // 변경된 상태 저장

        return ProposalSendResponse.from(user, proposal);
    }

    public ProposalResponse getProposal(Long approvalId) {

        ApprovalProposal approvalProposal = findProposal(approvalId);
        return ProposalResponse.of(approvalProposal);
    }

    public ProposalResponseList getAllProposal(Long progressId) {

        ProjectProgress progress = findProgress(progressId);
        List<ApprovalProposal> approvalProposalList = approvalProposalRepository.findByProjectProgress(progress);

        return ProposalResponseList.from(approvalProposalList);
    }

    /**
     * 승인요청 상태 관련 로직
      */

    @Transactional
    public void modifyProposalStatus(Long proposalId) {
        ApprovalProposal proposal = findProposal(proposalId);

        List<ApprovalApprover> approvers = findApprovers(proposal);

        Map<Long, List<ApprovalDecision>> decisionsByApproverId = findDecisionsGroupedByApprovers(approvers);

        ApprovalProposalStatus proposalStatus = determineProposalStatus(approvers, decisionsByApproverId);

        proposal.setProposalStatus(proposalStatus);
    }

    public ProposalStatusResponse getProposalStatus(Long approvalId) {
        ApprovalProposal proposal = findProposal(approvalId);
        List<ApprovalApprover> approvers = findApprovers(proposal);

        int totalApprover = approvers.size();

        int approvedApproverCount = (int) approvers.stream()
            .filter(a -> a.getApproverStatus() == ApprovalApproverStatus.APPROVER_APPROVED)
            .count();

        int modificationRequestedApproverCount = (int) approvers.stream()
            .filter(a -> a.getApproverStatus() == ApprovalApproverStatus.APPROVER_REJECTED)
            .count();

        int waitingApproverCount = (int) approvers.stream()
            .filter(a -> a.getApproverStatus() == ApprovalApproverStatus.NOT_RESPONDED)
            .count();

        checkApproverCounts(totalApprover, approvedApproverCount, modificationRequestedApproverCount, waitingApproverCount);

        return ProposalStatusResponse.of(
            totalApprover,
            approvedApproverCount,
            modificationRequestedApproverCount,
            waitingApproverCount,
            proposal.getProposalStatus()
        );
    }

    private ApprovalProposalStatus determineProposalStatus(
        List<ApprovalApprover> approvers,
        Map<Long, List<ApprovalDecision>> decisionsByApproverId
    ) {
        boolean anyRejected = approvers.stream()
            .anyMatch(approver -> hasRejectedDecision(decisionsByApproverId.get(approver.getId())));

        boolean allApproved = approvers.stream()
            .allMatch(approver -> hasApprovedDecision(decisionsByApproverId.get(approver.getId())));

        if (anyRejected) {
            return ApprovalProposalStatus.FINAL_REJECTED;
        } else if (allApproved) {
            return ApprovalProposalStatus.FINAL_APPROVED;
        } else {
            return ApprovalProposalStatus.UNDER_REVIEW;
        }
    }

    private boolean hasApprovedDecision(List<ApprovalDecision> decisions) {
        if (decisions == null || decisions.isEmpty()) {
            return false; // 미응답이면 승인 아님
        }
        return decisions.stream()
            .anyMatch(d -> d.getDecisionStatus() == ApprovalDecisionStatus.APPROVED);
    }

    private boolean hasRejectedDecision(List<ApprovalDecision> decisions) {
        if (decisions == null || decisions.isEmpty()) {
            return false; // 미응답이면 거절 아님
        }
        return decisions.stream()
            .anyMatch(d -> d.getDecisionStatus() == ApprovalDecisionStatus.REJECTED);
    }

    private Map<Long, List<ApprovalDecision>> findDecisionsGroupedByApprovers(List<ApprovalApprover> approvers) {
        List<Long> approverIds = approvers.stream()
            .map(ApprovalApprover::getId)
            .toList();

        List<ApprovalDecision> decisions = approvalDecisionRepository.findByApprovalApproverIdIn(approverIds);

        return decisions.stream()
            .collect(Collectors.groupingBy(d -> d.getApprovalApprover().getId()));
    }

    private void findProjectUser(User user, Long projectId) {

        projectUserRepository.findByUserIdAndProjectId(user.getId(), projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    }

    private ProjectProgress findProgress(Long progressId) {

        return progressRepository.findById(progressId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }

    private ApprovalProposal findProposal(Long approvalId) {

        return approvalProposalRepository.findById(approvalId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_APPROVAL_PROPOSAL));
    }

    private List<ApprovalApprover> findApprovers(ApprovalProposal proposal) {

        return approvalApproverRepository.findByApprovalProposal(proposal);
    }

    private void checkApproverCounts(
        int totalApproverCount,
        int approvedApproverCount,
        int modificationRequestedApproverCount,
        int waitingApproverCount
    ) {
        int sum = approvedApproverCount + modificationRequestedApproverCount + waitingApproverCount;
        if (sum != totalApproverCount) {
            throw new IllegalStateException(
                String.format(
                    "승인권자 상태 합계(%d)가 총 승인권자 수(%d)와 일치하지 않습니다.",
                    sum,
                    totalApproverCount
                )
            );
        }
    }

    private void checkUserPermission(User user, Long projectId) {

        if (isAdmin(user)) {
            return;
        }
        if (isDeveloper(user)) {
            findProjectUser(user, projectId);
        } else {
            throw new CustomException(CustomErrorCode.YOUR_ARE_NOT_DEVELOPER);
        }
    }

    private boolean isAdmin(User user) {

        return user.getRole()
            .toString()
            .equals("ADMIN");
    }

    private boolean isDeveloper(User user) {

        return user.getRole() != null && user.getRole()
            .name()
            .equals("DEVELOPER");
    }
}
