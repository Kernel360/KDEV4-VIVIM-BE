package com.welcommu.moduleservice.file.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.modulerepository.file.FileRepository;
import com.welcommu.moduleservice.file.dto.FileCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {


    private final AmazonS3 amazonS3;
    private final FileRepository fileRepository;

    private final String bucketName = "vivim-s3";

    public void createPostFile(Long postId, MultipartFile file) throws IOException {
        // 파일 S3 경로 생성
        String key = "posts/" + postId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        // 메타데이터 구성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        // S3에 업로드
        amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
        // S3 URL 생성
        String fileUrl = amazonS3.getUrl(bucketName, key).toString();
        // DB에 저장
        File newFile=FileCreateRequest.toEntity(file,fileUrl);
        fileRepository.save(newFile);
    }
    public void createChecklistFile(Long postId, MultipartFile file) throws IOException {
        // 파일 S3 경로 생성
        String key = "posts/" + postId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        // 메타데이터 구성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        // S3에 업로드
        amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
        // S3 URL 생성
        String fileUrl = amazonS3.getUrl(bucketName, key).toString();
        // DB에 저장
        File newFile=FileCreateRequest.toEntity(file,fileUrl);
        fileRepository.save(newFile);
    }
    public List<File> getFilesForPost(Long postId) {
        return fileRepository.findByReferenceTypeAndReferenceId(ReferenceType.POST, postId);
    }

    public List<File> getFilesForApproval(Long approvalId) {
        return fileRepository.findByReferenceTypeAndReferenceId(ReferenceType.APPROVAL, approvalId);
    }
    public void deleteFile(Long projectId, Long fileId) {

    }
}
