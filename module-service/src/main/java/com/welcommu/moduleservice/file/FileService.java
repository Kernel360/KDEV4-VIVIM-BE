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
public class FileService {

    private final FileRepository fileRepository;


    public void createMultipartPostFile(MultipartFileMetadataRequest request,
        Long postId, String fileUrl) {
        File newFile = request.toEntity(request, ReferenceType.POST, postId, fileUrl);
        fileRepository.save(newFile);

    }

    public void createPostFile(FileRequest request, Long postId) {

        File newFile = request.toEntity(request, ReferenceType.POST, postId);
        fileRepository.save(newFile);
    }

    public void createApprovalFile(FileRequest request, Long approvalId) {
        File newFile = request.toEntity(request, ReferenceType.APPROVAL, approvalId);
        fileRepository.save(newFile);
    }

    public void createDecisionFile(FileRequest request, Long decisionId) {
        File newFile = request.toEntity(request, ReferenceType.DECISION, decisionId);
        fileRepository.save(newFile);
    }

    public List<FileListResponse> getPostFiles(Long postId) {
        List<File> files = fileRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            postId, ReferenceType.POST);
        return files.stream()
            .map(FileListResponse::from)
            .collect(Collectors.toList());
    }

    public List<FileListResponse> getApprovalFiles(Long approvalId) {
        List<File> files = fileRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            approvalId, ReferenceType.APPROVAL);
        return files.stream()
            .map(FileListResponse::from)
            .collect(Collectors.toList());
    }

    public List<FileListResponse> getDecisionFiles(Long decisionId) {
        List<File> files = fileRepository.findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(
            decisionId, ReferenceType.DECISION);
        return files.stream()
            .map(FileListResponse::from)
            .collect(Collectors.toList());
    }

    public File getFileInfo(Long fileId) {
        return fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_FILE));
    }

    @Transactional
    public void deleteFile(Long fileId) {
        File existingFile = fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_FILE));
        existingFile.setDeletedAt();
    }

}
