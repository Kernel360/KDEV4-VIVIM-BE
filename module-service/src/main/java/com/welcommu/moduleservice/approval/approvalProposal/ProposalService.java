package com.welcommu.moduleservice.approval.approvalProposal;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalApprover;
import com.welcommu.moduledomain.approval.ApprovalApproverStatus;
import com.welcommu.moduledomain.approval.ApprovalDecision;
import com.welcommu.moduledomain.approval.ApprovalProposal;
import com.welcommu.moduledomain.approval.ApprovalProposalStatus;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.notification.NotificationType;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.approval.ApprovalApproverRepository;
import com.welcommu.moduleinfra.approval.ApprovalDecisionRepository;
import com.welcommu.moduleinfra.approval.ApprovalProposalRepository;
import com.welcommu.moduleinfra.company.CompanyRepository;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectprogress.ProjectProgressRepository;
import com.welcommu.moduleinfra.user.UserRepository;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalCreateRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalModifyRequest;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalResponseList;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalSendResponse;
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalStatusResponse;
import com.welcommu.moduleservice.notification.NotificationService;
import com.welcommu.moduleservice.notification.dto.NotificationRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ProposalService {

    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final ApprovalApproverRepository approvalApproverRepository;
    private final NotificationService notificationService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long createProposal(User creator, Long progressId, ProposalCreateRequest request) {
        ProjectProgress progress = findProgress(progressId);
        checkUserPermission(creator, progress.getProject()
            .getId());

        ApprovalProposal approvalProposal = request.toEntity(creator, progress);
        approvalProposalRepository.save(approvalProposal);

        //참여자들에게 알림 전송
        List<ProjectUser> participants = projectUserRepository.findByProject(progress.getProject());

        for (ProjectUser participant : participants) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(participant.getUser().getId())
                .content(String.format("%s님이 '%s' 승인 요청을 생성했습니다.", creator.getName(),
                    request.getTitle()))
                .type(NotificationType.PROPOSAL_CREATED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

        //관리자에게도 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(String.format("%s님이 '%s' 승인 요청을 생성했습니다..", creator.getName(),
                    approvalProposal.getTitle()))
                .type(NotificationType.PROPOSAL_CREATED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

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

        //참여자들에게 알림 전송
        List<ProjectUser> participants = projectUserRepository.findByProject(progress.getProject());

        for (ProjectUser participant : participants) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(participant.getUser().getId())
                .content(String.format("%s님이 '%s' 승인 요청을 수정했습니다.", user.getName(),
                    request.getTitle()))
                .type(NotificationType.PROPOSAL_MODIFIED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

        //관리자에게도 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(String.format("%s님이 '%s' 승인 요청를 수정했습니다.", user.getName(),
                    approvalProposal.getTitle()))
                .type(NotificationType.PROPOSAL_MODIFIED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }
    }

    @Transactional
    public void deleteProposal(User user, Long approvalId) {

        ApprovalProposal approvalProposal = findProposal(approvalId);

        //참여자들에게 알림 전송
        List<ProjectUser> participants = projectUserRepository.findByProject(
            approvalProposal.getProjectProgress().getProject());

        for (ProjectUser participant : participants) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(participant.getUser().getId())
                .content(String.format("%s님이 '%s' 승인 요청을 삭제했습니다.", user.getName(),
                    approvalProposal.getTitle()))
                .type(NotificationType.PROPOSAL_DELETED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

        //관리자에게도 알림 전송
        List<Company> adminCompanies = companyRepository.findByCompanyRole(CompanyRole.ADMIN);
        List<Long> adminCompanyIds = adminCompanies.stream()
            .map(Company::getId)
            .toList();

        List<User> adminUsers = userRepository.findByCompanyIdIn(adminCompanyIds);
        for (User admin : adminUsers) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                .receiverId(admin.getId())
                .content(String.format("%s님이 '%s' 승인 요청을 삭제했습니다.", user.getName(),
                    approvalProposal.getTitle()))
                .type(NotificationType.PROPOSAL_DELETED)
                .typeId(approvalProposal.getId())
                .build();
            notificationService.sendNotification(notificationRequest);
        }

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
        approvalProposalRepository.save(proposal);
        return ProposalSendResponse.from(user, proposal);
    }

    public ProposalResponse getProposal(Long approvalId) {

        ApprovalProposal approvalProposal = findProposal(approvalId);
        return ProposalResponse.of(approvalProposal);
    }

    public ProposalResponseList getAllProposal(Long progressId) {

        ProjectProgress progress = findProgress(progressId);
        List<ApprovalProposal> approvalProposalList = approvalProposalRepository.findByProjectProgress(
            progress);

        return ProposalResponseList.from(approvalProposalList);
    }

    public ProposalResponseList getRecentProposals(User user) {
        List<Long> projectIds = projectUserRepository.findByUserId(user.getId()).stream()
            .map(pu -> pu.getProject().getId())
            .distinct()
            .toList();

        if (projectIds.isEmpty()) {
            return ProposalResponseList.from(List.of());
        }

        List<ApprovalProposal> proposals = approvalProposalRepository
            .findByProjectProgress_Project_IdInOrderByCreatedAtDesc(
                projectIds,
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
            );

        return ProposalResponseList.from(proposals);
    }

    /**
     * 승인요청 상태 관련 로직
     */

    // 실제 ApprovalProposal(승인요청)의 상태를 DB에 반영하는 메서드
    // 즉, 계산한 상태를 실제 ApprovalProposal 엔티티에 setProposalStatus() 해서 변경하는 역할
    @Transactional
    public void modifyProposalStatus(Long proposalId) {
        ApprovalProposal proposal = findProposal(proposalId);

        List<ApprovalApprover> approvers = findApprovers(proposal);

        Map<Long, List<ApprovalDecision>> decisionsByApproverId = findDecisionsGroupedByApprovers(
            approvers);

        ApprovalProposalStatus proposalStatus = determineProposalStatus(approvers,
            decisionsByApproverId);

        proposal.setProposalStatus(proposalStatus);
    }

    // 승인요청(Proposal)이 어떤 최종 상태를 가져야 하는지 계산하는 메서드
    private ApprovalProposalStatus determineProposalStatus(
        List<ApprovalApprover> approvers,
        Map<Long, List<ApprovalDecision>> decisionsByApproverId
    ) {
        // 하나라도 APPROVER_REJECTED면 무조건 FINAL_REJECTED
        boolean anyRejected = approvers.stream()
            .anyMatch(approver -> approver.getApproverStatus()
                == ApprovalApproverStatus.APPROVER_REJECTED);

        if (anyRejected) {
            return ApprovalProposalStatus.FINAL_REJECTED;
        }

        // 전부 APPROVER_APPROVED 면 FINAL_APPROVED
        boolean allApproved = approvers.stream()
            .allMatch(approver -> approver.getApproverStatus()
                == ApprovalApproverStatus.APPROVER_APPROVED);

        if (allApproved) {
            return ApprovalProposalStatus.FINAL_APPROVED;
        }

        // 그 외에는 UNDER_REVIEW
        return ApprovalProposalStatus.UNDER_REVIEW;
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

        checkApproverCounts(totalApprover, approvedApproverCount,
            modificationRequestedApproverCount, waitingApproverCount);

        return ProposalStatusResponse.of(
            totalApprover,
            approvedApproverCount,
            modificationRequestedApproverCount,
            waitingApproverCount,
            proposal.getProposalStatus()
        );
    }

    private Map<Long, List<ApprovalDecision>> findDecisionsGroupedByApprovers(
        List<ApprovalApprover> approvers) {
        List<Long> approverIds = approvers.stream()
            .map(ApprovalApprover::getId)
            .toList();

        List<ApprovalDecision> decisions = approvalDecisionRepository.findByApprovalApproverIdIn(
            approverIds);

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
