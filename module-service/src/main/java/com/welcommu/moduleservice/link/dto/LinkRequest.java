package com.welcommu.moduleservice.link.dto;


import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import lombok.Getter;

@Getter
public class LinkRequest {

    private String title;
    private String url;


    public Link toEntity(LinkRequest request, ReferenceType referenceType, Long referenceId) {
        return Link.builder()
            .title(request.getTitle())
            .url(request.getUrl())
            .referenceType(referenceType)
            .referenceId(referenceId)
            .build();
    }
}
