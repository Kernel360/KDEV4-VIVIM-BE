package com.welcommu.modulerepository.project;

import com.welcommu.moduledomain.project.Project;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

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
}
