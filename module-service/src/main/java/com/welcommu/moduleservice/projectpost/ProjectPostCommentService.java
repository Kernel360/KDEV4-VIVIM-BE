package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.CompanyRole;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectpost.ProjectPostCommentRepository;
import com.welcommu.modulerepository.projectpost.ProjectPostRepository;
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
public class ProjectPostCommentService {

    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectPostCommentAuditService projectPostCommentAuditService;

    @Transactional
    public void createComment(User user, Long postId, ProjectPostCommentRequest request,
        String clientIp) {
        ProjectPostComment newComment = request.toEntity(user, postId, request, clientIp);
        ProjectPost post = projectPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        checkUserPermission(user, post.getProject().getId());

        Long creatorId = user.getId();
        ProjectPostComment savedComment = projectPostCommentRepository.save(newComment);
        projectPostCommentAuditService.createAuditLog(ProjectPostCommentSnapshot.from(savedComment),
            creatorId);
    }

    @Transactional
    public void modifyComment(User user, Long postId, Long commentId,
        ProjectPostCommentRequest request) {

        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));

        Long modifierId = user.getId();

        ProjectPostCommentSnapshot beforeSnapshot = ProjectPostCommentSnapshot.from(
            existingComment);

        existingComment.setContent(request.getContent());
        existingComment.setModifiedAt();

        ProjectPostCommentSnapshot afterSnapshot = ProjectPostCommentSnapshot.from(existingComment);
        projectPostCommentAuditService.modifyAuditLog(beforeSnapshot, afterSnapshot, modifierId);
    }

    @Transactional(readOnly = true)
    public List<ProjectPostCommentListResponse> getCommentList(Long projectPostId) {
        List<ProjectPostComment> comments = projectPostCommentRepository.findAllByProjectPostIdAndDeletedAtIsNull(
            projectPostId);
        return comments.stream()
            .map(ProjectPostCommentListResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(User user, Long postId, Long commentId) {
        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        existingComment.setDeletedAt();

        Long deleterId = user.getId();

        projectPostCommentAuditService.deleteAuditLog(
            ProjectPostCommentSnapshot.from(existingComment), deleterId);
    }

    private void checkUserPermission(User user, Long projectId) {
        if (projectUserRepository.findByUserIdAndProjectId(user.getId(), projectId).isEmpty() && !(
            user.getCompany().getCompanyRole() == CompanyRole.ADMIN)) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER);
        }
    }
}
