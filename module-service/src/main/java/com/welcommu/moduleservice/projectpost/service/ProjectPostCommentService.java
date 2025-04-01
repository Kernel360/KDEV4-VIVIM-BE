package com.welcommu.moduleservice.projectpost.service;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostCommentRepository;
import com.welcommu.moduleservice.projectpost.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostCommentService {
    private final ProjectPostCommentRepository projectPostCommentRepository;

    @Transactional
    public void createComment(Long postId, ProjectPostCommentRequest request, String clientIp) {
        ProjectPostComment newComment= request.toEntity(postId, request, clientIp);
        projectPostCommentRepository.save(newComment);
    }

    @Transactional
    public void modifyComment(Long postId, Long commentId, ProjectPostCommentRequest request) {

        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if (!existingComment.getProjectPostId().equals(postId) ) {
            throw new IllegalArgumentException("게시글의 Id가 댓글과 일치하지 않습니다.");
        }

        existingComment.setComment(request.getComment());
        existingComment.setModifiedAt();
        existingComment.setModifierId(1L);//테스트용
    }

    @Transactional(readOnly = true)
    public List<ProjectPostCommentListResponse> getCommentList(Long projectPostId) {
        List<ProjectPostComment> comments = projectPostCommentRepository.findAllByProjectPostId(projectPostId);
        return comments.stream()
                .map(ProjectPostCommentListResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        ProjectPostComment existingComment = projectPostCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        if (!existingComment.getProjectPostId().equals(postId)) {
            throw new IllegalArgumentException("게시글의 ID가 댓글과 일치하지 않습니다.");
        }
        existingComment.setDeletedAt();
    }
}
