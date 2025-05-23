package com.welcommu.moduleservice.file.dto;

import com.welcommu.moduledomain.file.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileListResponse {

    Long id;
    String fileName;

    public static FileListResponse from(File file) {
        return new FileListResponse(
            file.getId(),
            file.getFileName()
        );
    }

}
