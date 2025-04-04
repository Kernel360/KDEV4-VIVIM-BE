package com.welcommu.moduleservice.link.dto;


import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.link.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkListResponse {
    String title;
    String url;

    public static LinkListResponse from(Link link) {
        return new LinkListResponse(
                link.getTitle(),
                link.getUrl()
        );
    }
}
