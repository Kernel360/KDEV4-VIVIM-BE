package com.welcommu.modulerepository.link;

import com.welcommu.moduledomain.file.ReferenceType;
import com.welcommu.moduledomain.link.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findAllByReferenceIdAndReferenceTypeAndDeletedAtIsNull(Long postId,
        ReferenceType referenceType);
}
