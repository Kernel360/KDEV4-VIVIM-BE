package com.welcommu.moduleservice.projectpost.service;

import com.welcommu.moduledomain.projectpost.entity.ProjectPostComment;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostCommentRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectPostCommentService {
    private final ProjectPostCommentRepository projectPostCommentRepository;

    @Transactional
    public void createComment(Long postId, ProjectPostCommentRequest request){
        ProjectPostComment newComment= request.toEntity(postId, request);
        projectPostCommentRepository.save(newComment);
    }

}
