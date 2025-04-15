package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.modulecommon.logging.LogAudit;
import com.welcommu.modulecommon.logging.enums.ActionType;
import com.welcommu.modulecommon.logging.enums.TargetType;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.user.User;
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
    @LogAudit(targetType = TargetType.POST, actionType = ActionType.CREATE)
    public Long createPost(User user, Long projectId, ProjectPostRequest request, String clientIp) {
        ProjectPost newPost = request.toEntity(user, projectId, request, clientIp);

        projectPostRepository.save(newPost);
        return  newPost.getId();
    }

    @Transactional
    @LogAudit(targetType = TargetType.POST, actionType = ActionType.UPDATE)
    public void modifyPost(Long projectId, Long postId, ProjectPostRequest request) {

        ProjectPost existingPost= projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setProjectPostStatus(request.getProjectPostStatus());
        existingPost.setModifiedAt();
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
    @LogAudit(targetType = TargetType.POST, actionType = ActionType.DELETE)
    public void deletePost(Long projectId, Long postId) {
        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_POST));
        existingPost.setDeletedAt();
    }
}
