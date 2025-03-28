package com.welcommu.moduleservice.projectpost.service;

import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
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
    public void createComment(Long postId, ProjectPostCommentCreateRequest request){
        ProjectPostComment newComment= request.toEntity(postId, request);
        projectPostCommentRepository.save(newComment);
    }

    @Transactional
    public void modifyComment(Long postId, Long commentId, ProjectPostCommentModifyRequest request) {

        ProjectPostComment existingComment = projectPostCommentRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!existingComment.getProjectPostId().equals(postId) || existingComment.getId().equals(commentId)) {
            throw new IllegalArgumentException(" 커멘트 ID가 게시글과 일치하지 않습니다.");
        }
        request.modifyProjectPostComment(existingComment, request);
    }

    @Transactional(readOnly = true)
    public List<ProjectPostCommentListResponse> getCommentList(Long projectPostId) {
        List<ProjectPostComment> comments = projectPostCommentRepository.findAllByProjectPostId(projectPostId);
        return comments.stream()
                .map(ProjectPostCommentListResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, ProjectPostCommentDeleteRequest request) {
        ProjectPostComment existingComment = projectPostCommentRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!existingComment.getProjectPostId().equals(postId)) {
            throw new IllegalArgumentException("프로젝트 ID가 게시글과 일치하지 않습니다.");
        }
        request.deleteTo(existingComment);
    }
}
