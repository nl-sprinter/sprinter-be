package com.nl.sprinterbe.domain.contribution.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.contribution.dto.ContributionDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContributionRepository {
    //통계 쿼리 위치 애매해서 일단 따로 빼긴했는데 QueryDSL 안쓸려니 애~매
    @PersistenceContext
    private EntityManager em;

    public List<ContributionDto> findUserContributionBySprintAndProject(Long projectId, Long sprintId) {
        return em.createQuery("""
            SELECT new com.nl.sprinterbe.domain.contribution.dto.ContributionDto(
                u.userId, u.nickname, SUM(b.weight))
            FROM UserBacklog ub
            JOIN ub.user u
            JOIN ub.backlog b
            JOIN b.sprint s
            JOIN s.project p
            WHERE b.isFinished = true
              AND s.sprintId = :sprintId
              AND p.projectId = :projectId
            GROUP BY u.userId, u.nickname
        """, ContributionDto.class)
                .setParameter("projectId", projectId)
                .setParameter("sprintId", sprintId)
                .getResultList();
    }

    public List<ContributionDto> findUserContributionByProject(Long projectId) {
        return em.createQuery("""
            SELECT new com.nl.sprinterbe.domain.contribution.dto.ContributionDto(u.userId,u.nickname,SUM(b.weight))
            FROM UserBacklog ub
            JOIN ub.user u
            JOIN ub.backlog b
            JOIN b.sprint s
            JOIN s.project p 
            WHERE b.isFinished = true
            AND p.projectId = :projectId
            GROUP BY u.userId, u.nickname
        """, ContributionDto.class)
                .setParameter("projectId",projectId)
                .getResultList();
    }

}
