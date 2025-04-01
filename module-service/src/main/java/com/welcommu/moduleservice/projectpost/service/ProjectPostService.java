package com.welcommu.moduleservice.projectpost.service;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;

    public void createPost(Long projectId, ProjectPostRequest request, String clientIp) {
        ProjectPost newPost = request.toEntity(projectId, request, clientIp);
        projectPostRepository.save(newPost);
    }

    @Transactional
    public void modifyPost(Long projectId, Long postId, ProjectPostRequest request) {

        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!existingPost.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("프로젝트 ID가 게시글과 일치하지 않습니다.");
        }

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setProjectPostStatus(request.getProjectPostStatus());
        existingPost.setModifiedAt();
        existingPost.setModifierId(1L);//테스트용


    }

    @Transactional(readOnly = true)
    public List<ProjectPostListResponse> getPostList(Long projectId) {
        List<ProjectPost> posts = projectPostRepository.findAllByProjectId(projectId);
        return posts.stream()
                .map(ProjectPostListResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        existingPost.setDeletedAt();
    }
}
