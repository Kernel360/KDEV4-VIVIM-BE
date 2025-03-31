package com.welcommu.modulerepository.file;

import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByReferenceTypeAndReferenceId(ReferenceType referenceType, Long postId);
}
