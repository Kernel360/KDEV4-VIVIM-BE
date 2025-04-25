package com.welcommu.moduleservice.projectpost.dto;


import com.welcommu.moduledomain.projectUser.ProjectUser;
import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.moduledomain.user.User;
import com.welcommu.moduleinfra.project.ProjectUserRepository;
import com.welcommu.moduleinfra.projectpost.ProjectPostRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentUserPostListRequest {

    private User user;

    public List<ProjectPostListResponse> getRecentUserPostList(
        ProjectUserRepository projectUserRepository,
        ProjectPostRepository projectPostRepository) {

        List<ProjectUser> projectUsers = projectUserRepository.findByUser(this.user);

        List<Long> projectIds = projectUsers.stream()
            .map(projectUser -> projectUser.getProject().getId())
            .collect(Collectors.toList());

        List<ProjectPost> posts = projectPostRepository.findTop5ByProject_IdInOrderByCreatedAtDesc(
            projectIds);

        List<ProjectPost> latestPosts = posts.stream()
            .sorted(Comparator.comparing(ProjectPost::getCreatedAt).reversed())
            .limit(5)
            .toList();

        return latestPosts.stream()
            .map(ProjectPostListResponse::from)
            .collect(Collectors.toList());
    }
}
