package com.welcommu.moduleapi.project;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ProjectRequest {
    private String name;
    private String description;

    private List<Long> developerIds;
    private List<Long> clientIds;


}
