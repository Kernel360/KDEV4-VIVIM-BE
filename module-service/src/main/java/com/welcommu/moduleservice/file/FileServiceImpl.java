package com.welcommu.moduleservice.file;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduleinfra.file.FileRepository;
import com.welcommu.moduleservice.file.dto.FileListResponse;
import com.welcommu.moduleservice.file.dto.FileRequest;
import com.welcommu.moduleservice.file.dto.MultipartFileMetadataRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public void createMultipartFile(MultipartFileMetadataRequest request,
        ReferenceType referenceType, Long referenceId, String fileUrl) {
        File newFile = request.toEntity(request, referenceType, referenceId, fileUrl);
        fileRepository.save(newFile);
    }

    @Override
    public void createFile(FileRequest request, ReferenceType referenceType, Long referenceId)
        throws CustomException {
        File newFile = request.toEntity(request, referenceType, referenceId);
        fileRepository.save(newFile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileListResponse> getFilesByReference(ReferenceType referenceType,
        Long referenceId) {
        return fileRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
                referenceId, referenceType)
            .stream()
            .map(FileListResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public File getFileInfo(Long fileId) throws CustomException {
        return fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_FILE));
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId) throws CustomException {
        File existingFile = fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_FILE));
        existingFile.setDeletedAt();
    }
}
