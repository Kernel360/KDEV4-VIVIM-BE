package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.user.User;
import com.welcommu.modulerepository.project.ProjectUserRepository;
import com.welcommu.modulerepository.projectpost.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;
    private final ProjectUserRepository projectUserRepository;

    public Long createPost(User user, Long projectId, ProjectPostRequest request, String clientIp) {
        ProjectPost newPost = request.toEntity(user, projectId, request, clientIp);

        projectPostRepository.save(newPost);
        return newPost.getId();
    }

    @Transactional
    public void modifyPost(Long projectId, Long postId, ProjectPostRequest request) {

        ProjectPost existingPost = projectPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setProjectPostStatus(request.getProjectPostStatus());
        existingPost.setModifiedAt();
    }


    public List<ProjectPostListResponse> getPostList(Long projectId) {
        List<ProjectPost> posts = projectPostRepository.findAllByProjectIdAndDeletedAtIsNull(
            projectId);

        return posts.stream()
            .map(ProjectPostListResponse::from)
            .collect(Collectors.toList());
    }

    public List<ProjectPostListResponse> getRecentPostList() {
        List<ProjectPost> posts = projectPostRepository.findTop5ByDeletedAtIsNullOrderByCreatedAtDesc();

        return posts.stream()
            .map(ProjectPostListResponse::from)
            .collect(Collectors.toList());
    }

    public List<ProjectPostListResponse> getRecentUserPostList(User user) {
        // 1. 사용자가 참여한 프로젝트 ID 리스트 조회
        List<ProjectUser> projectUsers = projectUserRepository.findByUser(user);

        // 2. 프로젝트 ID 리스트 추출
        List<Long> projectIds = projectUsers.stream()
            .map(projectUser -> projectUser.getProject().getId())
            .collect(Collectors.toList());

        // 3. 참여한 프로젝트들의 최신 5개의 게시글을 조회
        List<ProjectPost> posts = projectPostRepository.findTop5PostsByProjectIds(projectIds);

        // 4. 최신 5개의 게시글만 가져오기
        List<ProjectPost> latestPosts = posts.stream()
            .limit(5) // 리스트에서 5개만 가져옵니다.
            .toList();

        // 5. 결과 변환
        return latestPosts.stream()
            .map(ProjectPostListResponse::from)
            .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ProjectPostDetailResponse getPostDetail(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        return ProjectPostDetailResponse.from(existingPost);
    }

    @Transactional
    public void deletePost(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));
        existingPost.setDeletedAt();
    }
}
