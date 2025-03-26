package com.welcommu.moduleservice.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPostComment;
import com.welcommu.modulerepository.projectpost.ProjectPostCommentRepository;
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
