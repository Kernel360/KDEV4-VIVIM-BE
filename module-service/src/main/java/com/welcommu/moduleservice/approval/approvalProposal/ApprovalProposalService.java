package com.welcommu.moduleservice.approval.approvalProposal;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.approval.ApprovalProposal;
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
import com.welcommu.moduleservice.approval.approvalProposal.dto.ProposalSendResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ApprovalProposalService {
    
    private final ProjectProgressRepository progressRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ApprovalProposalRepository approvalProposalRepository;
    private final ApprovalApproverRepository approvalApproverRepository;
    
    public Long createProposal(User creator, Long progressId, ProposalCreateRequest request) {
        ProjectProgress progress = findProgress(progressId);
        checkUserPermission(creator, progress.getProject().getId());
        
        ApprovalProposal approvalProposal = request.toEntity(creator, progress);
        approvalProposalRepository.save(approvalProposal);
        return approvalProposal.getId();
    }
    
    public void modifyProposal(User user, Long approvalId, ProposalModifyRequest request) {
        
        ApprovalProposal approvalProposal = findProposal(approvalId);
        ProjectProgress progress = findProgress(approvalProposal.getProjectProgress().getId());
        checkUserPermission(user, progress.getProject().getId());
        
        if (request.getTitle() != null) {
            approvalProposal.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            approvalProposal.setContent(request.getContent());
        }
        approvalProposalRepository.save(approvalProposal);
    }
    
    public void deleteProposal(Long approvalId) {
        
        ApprovalProposal approvalProposal = findProposal(approvalId);
        approvalProposalRepository.delete(approvalProposal);
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
    
    @Transactional
    public ProposalSendResponse sendProposal(User user, Long approvalId) {
        ApprovalProposal proposal = findProposal(approvalId);
        checkUserPermission(user, proposal.getProjectProgress().getProject().getId());
        
        // 승인권자 지정 여부 확인
        boolean hasApprovers = approvalApproverRepository.existsByApprovalProposal(proposal);
        if (!hasApprovers) {
            throw new CustomException(CustomErrorCode.NO_APPROVER_ASSIGNED);
        }
        
        return ProposalSendResponse.from(user, proposal);
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
        return user.getRole().toString().equals("ADMIN");
    }
    
    private boolean isDeveloper(User user) {
        return user.getRole() != null && user.getRole().name().equals("DEVELOPER");
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
}
