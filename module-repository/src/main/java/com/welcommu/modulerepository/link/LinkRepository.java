package com.welcommu.modulerepository.link;

import com.welcommu.moduledomain.file.File;
import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(Long postId, ReferenceType referenceType);
}
