package com.welcommu.moduleservice.file;

import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import com.welcommu.moduleservice.file.dto.FileRequest;
import com.welcommu.moduleservice.file.dto.MultipartFileMetadataRequest;
import java.util.List;

public interface FileService {

    void createMultipartFile(MultipartFileMetadataRequest request, ReferenceType referenceType,
        Long referenceId, String fileUrl);

    void createFile(FileRequest request, ReferenceType referenceType, Long referenceId)
        throws CustomException;

    List<FileListResponse> getFilesByReference(ReferenceType referenceType, Long referenceId);

    File getFileInfo(Long fileId) throws CustomException;

    void deleteFile(Long fileId) throws CustomException;
}

