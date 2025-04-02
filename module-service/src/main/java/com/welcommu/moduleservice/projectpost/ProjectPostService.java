package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;

    public void createPost(Long projectId, ProjectPostRequest request, String clientIp) {
        ProjectPost newPost = request.toEntity(projectId, request, clientIp);

    public void createPost(Long projectId, ProjectPostRequest request) {
        ProjectPost newPost = request.toEntity(projectId, request);

        projectPostRepository.save(newPost);
    }

    @Transactional
    public void modifyPost(Long projectId, Long postId, ProjectPostRequest request) {

        ProjectPost existingPost= projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setProjectPostStatus(request.getProjectPostStatus());
        existingPost.setModifiedAt();
        existingPost.setModifierId(1L);//테스트용


    }



    public List<ProjectPostListResponse> getPostList(Long projectId) {
        List<ProjectPost> posts = projectPostRepository.findAllByProjectId(projectId);

        return posts.stream()
                .map(ProjectPostListResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ProjectPostDetailResponse getPostDetail(Long projectId, Long postId) {
        ProjectPost existingPost= projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        return ProjectPostDetailResponse.from(existingPost);
    }

    @Transactional
    public void deletePost(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));
        existingPost.setDeletedAt();
        existingPost.setDeleterId(1L);//테스트용

    @Transactional
    public void deletePost(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        existingPost.delete(LocalDateTime.now());

    }
}
