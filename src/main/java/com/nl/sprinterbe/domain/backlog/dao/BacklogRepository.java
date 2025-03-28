package com.nl.sprinterbe.domain.backlog.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long>, BacklogSearchQueryRepository {

    @Query("SELECT b FROM Backlog b" +
            " JOIN UserBacklog ub ON ub.backlog = b" +
            " WHERE b.sprint.project.projectId = :projectId" +
            " AND ub.user.userId = :userId" +
            " ORDER BY b.sprint.sprintOrder DESC, ub.userBacklogId DESC")
    List<Backlog> findUserBacklogsByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);


    Optional<Backlog> findByBacklogId(Long backlogId);
    
    boolean existsByBacklogId(Long backlogId);
    @Query("select i from Issue i where i.backlog.backlogId = :backlogId")
    List<Issue> findIssuesByBacklogId(@Param("backlogId") Long backlogId);

    @Query("select t from Task t where t.backlog.backlogId = :backlogId")
    List<Task> findTasksByBacklogId(@Param("backlogId") Long backlogId);

    @Query("SELECT up.user FROM UserProject up " +
            "WHERE up.project.projectId = :projectId " +
            "AND up.user.userId NOT IN (" +
            "   SELECT ub.user.userId FROM UserBacklog ub WHERE ub.backlog.backlogId = :backlogId" +
            ")")
    List<User> findUsersNotInBacklog(@Param("projectId") Long projectId,
                                     @Param("backlogId") Long backlogId);

    // 특정 sprintId에 속한 Backlog 중,
    // dailyScrumId로 연결된 Backlog를 제외한 목록을 조회

    @Query("SELECT b FROM Backlog b " +
            "WHERE b.sprint.sprintId = :sprintId " +  // Sprint에 속하는 Backlog 중
            "AND b.backlogId NOT IN (SELECT dsb.backlog.backlogId FROM DailyScrumBacklog dsb WHERE dsb.dailyScrum.dailyScrumId = :dailyScrumId)") // 특정 DailyScrum에 포함되지 않은 Backlog
    List<Backlog> findExcludingDailyScrum(@Param("sprintId") Long sprintId, @Param("dailyScrumId") Long dailyScrumId);

    @Query("SELECT b FROM Backlog b" +
            " join fetch b.sprint s" +
            " join fetch s.project p" +
            " WHERE p.projectId = :projectId")
    List<Backlog> findBacklogsByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT b FROM Backlog b" +
            " JOIN FETCH b.sprint s" +
            " JOIN FETCH s.project p" +
            " WHERE p.projectId=:projectId AND s.sprintId=:sprintId")
    List<Backlog> findBacklogsByProjectIdAndSprintId(@Param("projectId") Long projectId, @Param("sprintId") Long sprintId);

}
