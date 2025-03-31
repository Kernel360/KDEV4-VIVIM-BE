package com.welcommu.moduleapi.link;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/posts")
@RequiredArgsConstructor
public class LinkController {

}
