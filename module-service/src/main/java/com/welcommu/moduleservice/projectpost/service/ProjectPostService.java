package com.welcommu.moduleservice.projectpost.service;

import com.welcommu.moduledomain.projectpost.entity.ProjectPost;
import com.welcommu.modulerepository.projectpost.repository.ProjectPostRepository;
import com.welcommu.moduleservice.projectpost.dto.CreateProjectPostCommand;
import com.welcommu.moduleservice.projectpost.dto.ProjectPostListResult;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;

    public Long createPost(Long projectId, CreateProjectPostCommand command) {
        ProjectPost newPost = ProjectPost.builder()
                .projectId(projectId)
                .title(command.title())
                .content(command.content())
                .projectPostStatus(command.projectPostStatus())
                .creatorId(1L)
                .build();

        projectPostRepository.save(newPost);
        return newPost.getId();
    }

    @Transactional
    public Long modifyPost(Long projectId, Long postId, CreateProjectPostCommand command) {
        // 1. 기존 게시글 조회
        ProjectPost existingPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 2. 게시글 소속 프로젝트 일치 여부 체크 (선택적)
        if (!existingPost.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("프로젝트 ID가 게시글과 일치하지 않습니다.");
        }

        // 3. 내용 수정
        existingPost.modify(
                command.title(),
                command.content(),
                command.projectPostStatus()// 수정자 ID (임시값 또는 로그인 사용자)
        );

        // 4. 저장은 @Transactional 덕분에 자동 처리됨
        return existingPost.getId();
    }

    public List<ProjectPostListResult> getPostList(Long projectId) {
        List<ProjectPost> posts = projectPostRepository.findAllByProjectId(projectId);
        return posts.stream()
                .map(ProjectPostListResult::from)
                .collect(Collectors.toList());
    }

    public  Long deletePost(Long postId) {
        projectPostRepository.deleteById(postId);
        return postId;
    }
}
