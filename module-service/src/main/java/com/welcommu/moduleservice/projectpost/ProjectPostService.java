package com.welcommu.moduleservice.projectpost;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostDetailResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResponse;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostRequest;
import com.welcommu.moduleservice.projectpost.dto.RecentUserPostListRequest;
import java.util.List;

public interface ProjectPostService {

    Long createPost(User user, Long projectId, ProjectPostRequest request, String clientIp,
        Long creatorId) throws CustomException;

    void modifyPost(Long projectId, Long postId, ProjectPostRequest request,
        Long modifierId) throws CustomException;

    List<ProjectPostListResponse> getPostList(Long projectId);

    List<ProjectPostListResponse> getRecentPostList();

    List<ProjectPostListResponse> getRecentUserPostList(RecentUserPostListRequest request);

    ProjectPostDetailResponse getPostDetail(Long projectId, Long postId) throws CustomException;

    void deletePost(Long projectId, Long postId, Long deleterId) throws CustomException;

    void completeAnswer(Long postId, String answer) throws CustomException;
}
