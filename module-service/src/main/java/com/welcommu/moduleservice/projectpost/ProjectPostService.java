package com.welcommu.moduleservice.projectpost;

import com.welcommu.moduledomain.projectpost.ProjectPost;
import com.welcommu.modulerepository.projectpost.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return 1L;
    }
}
