package com.welcommu.moduleapi.project;

import com.welcommu.moduleservice.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

}
