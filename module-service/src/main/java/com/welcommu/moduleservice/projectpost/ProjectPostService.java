package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.modulerepository.projectpost.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;

    public Long createPost(Long projectId, ProjectPostRequest request, String clientIp) {
        ProjectPost newPost = request.toEntity(projectId, request, clientIp);

        projectPostRepository.save(newPost);
        return  newPost.getId();
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
        List<ProjectPost> posts = projectPostRepository.findAllByProjectIdAndDeletedAtIsNull(projectId);

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
    }
}
