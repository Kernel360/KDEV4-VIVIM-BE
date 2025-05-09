package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectpost.ProjectPostCommentRepository;
import com.welcommu.moduleinfra.projectpost.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.audit.ProjectPostCommentAuditService;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentRequest;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentSnapshot;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProjectPostCommentServiceImpl implements ProjectPostCommentService {

    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectPostCommentAuditService projectPostCommentAuditService;

    @Override
    @Transactional
    public void createComment(User user, Long postId, ProjectPostCommentRequest request,
        String clientIp) {
        ProjectPostComment newComment = request.toEntity(user, postId, request, clientIp);
        ProjectPost post = projectPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));
        checkUserPermission(user, post.getProject().getId());
        ProjectPostComment savedComment = projectPostCommentRepository.save(newComment);
        projectPostCommentAuditService.createAuditLog(
            ProjectPostCommentSnapshot.from(savedComment), user.getId());
    }

    @Override
    @Transactional
    public void modifyComment(User user, Long postId, Long commentId,
        ProjectPostCommentRequest request) {
        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        ProjectPostCommentSnapshot before = ProjectPostCommentSnapshot.from(existingComment);
        existingComment.setContent(request.getContent());
        existingComment.setModifiedAt();
        projectPostCommentAuditService.modifyAuditLog(
            before, ProjectPostCommentSnapshot.from(existingComment), user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectPostCommentListResponse> getCommentList(Long projectPostId) {
        return projectPostCommentRepository
            .findAllByProjectPostIdAndDeletedAtIsNull(projectPostId)
            .stream()
            .map(ProjectPostCommentListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(User user, Long postId, Long commentId) {
        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        existingComment.setDeletedAt();
        projectPostCommentAuditService.deleteAuditLog(
            ProjectPostCommentSnapshot.from(existingComment), user.getId());
    }

    private void checkUserPermission(User user, Long projectId) {
        boolean isMember = projectUserRepository
            .findByUserIdAndProjectId(user.getId(), projectId)
            .isPresent();
        if (!isMember && user.getCompany().getCompanyRole() != CompanyRole.ADMIN) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER);
        }
    }
}
