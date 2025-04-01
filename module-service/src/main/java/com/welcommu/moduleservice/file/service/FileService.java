package com.welcommu.moduleservice.file.service;


import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.modulerepository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    public void createPostFile(String fileName, String fileUrl, Long fileSize, Long postId) {

        File newFile= File.create(fileName, fileUrl, fileSize, ReferenceType.POST, postId);
        fileRepository.save(newFile);
    }
    public void createApprovalFile(String fileName, String fileUrl, Long fileSize, Long postId) {
        File newFile= File.create(fileName, fileUrl, fileSize, ReferenceType.POST, postId);
        fileRepository.save(newFile);
    }
}
