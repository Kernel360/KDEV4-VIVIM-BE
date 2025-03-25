package com.welcommu.moduledomain.projectProgess;

import com.welcommu.exception.CustomErrorCode;
import com.welcommu.exception.CustomException;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.projectProgess.dto.ProjectProgressRequest;
import com.welcommu.moduledomain.projectprogress.ProjectProgress;
import com.welcommu.modulerepository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectRepository repository;

    public void create(
        Long projectId,
        ProjectProgressRequest request
    ) {
        Project project = findProject(projectId);
        ProjectProgress projectProgress = request.toEntity(request.getName(), project);
    }

    private Project findProject(Long projectId) {
        return repository.findById(projectId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROGRESS));
    }
    private float find
}
