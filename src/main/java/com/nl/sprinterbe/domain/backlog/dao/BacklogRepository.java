package com.nl.sprinterbe.domain.backlog.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

    @Query("SELECT b from Backlog b WHERE b.sprint.sprintId = :sprintId order by b.sprint.sprintOrder ASC")
    Slice<Backlog> findBySprintIdDesc(@Param("sprintId") Long sprintId, Pageable pageable);

    /*@Query("SELECT b FROM Backlog b JOIN b.sprint s JOIN b.userBacklogs u WHERE s.project.projectId = :projectId AND u.user.userId = :userId order by b.sprint.sprintOrder ASC")
    Slice<Backlog> findByProjectIdDesc(@Param("projectId") Long projectId,@Param("userId") Long userId, Pageable pageable);*/

    @Query("SELECT b FROM Backlog b JOIN b.sprint s, UserBacklog ub " +
            "WHERE s.project.projectId = :projectId " +
            "AND ub.user.userId = :userId " +
            "AND ub.backlog = b " +
            "ORDER BY b.sprint.sprintOrder ASC")
    Slice<Backlog> findByProjectIdDesc(@Param("projectId") Long projectId,
                                       @Param("userId") Long userId,
                                       Pageable pageable);


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


}
