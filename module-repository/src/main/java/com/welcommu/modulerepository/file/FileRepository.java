package com.welcommu.modulerepository.file;

import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(Long referenceId,
        ReferenceType referenceType);
}
