package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Project> findAllByCompanyId(Long companyId) {
        String jpql = """
            SELECT DISTINCT p
            FROM Project p
            JOIN ProjectUser pu ON pu.project = p
            JOIN User u ON pu.user = u
            WHERE u.company.id = :companyId
        """;

        return em.createQuery(jpql, Project.class)
            .setParameter("companyId", companyId)
            .getResultList();
    }

    @Override
    public List<Object[]> findAllByCompanyIdWithMyRole(Long companyId, Long myUserId) {
        String jpql = """
        SELECT p, pu
        FROM Project p
        JOIN ProjectUser pu ON pu.project = p
        JOIN User u ON pu.user = u
        WHERE u.company.id = :companyId
    """;

        return em.createQuery(jpql, Object[].class)
            .setParameter("companyId", companyId)
            .getResultList();
    }

    @Override
    public Page<Project> searchByConditions(String name, String description, Boolean isDeleted, Pageable pageable) {
        StringBuilder sb = new StringBuilder("SELECT p FROM Project p WHERE 1=1");
        StringBuilder countSb = new StringBuilder("SELECT COUNT(p) FROM Project p WHERE 1=1");

        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            sb.append(" AND p.name LIKE :name");
            countSb.append(" AND p.name LIKE :name");
            params.put("name", "%" + name + "%");
        }

        if (description != null && !description.isBlank()) {
            sb.append(" AND p.description LIKE :description");
            countSb.append(" AND p.description LIKE :description");
            params.put("description", "%" + description + "%");
        }

        if (isDeleted != null) {
            sb.append(" AND p.isDeleted = :isDeleted");
            countSb.append(" AND p.isDeleted = :isDeleted");
            params.put("isDeleted", isDeleted);
        }

        sb.append(" ORDER BY p.id DESC");

        TypedQuery<Project> query = em.createQuery(sb.toString(), Project.class);
        TypedQuery<Long> countQuery = em.createQuery(countSb.toString(), Long.class);

        params.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, countQuery.getSingleResult());
    }
}
