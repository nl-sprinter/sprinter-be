package com.nl.sprinterbe.domain.backlog.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

    @Query("SELECT b from Backlog b WHERE b.sprint.sprintId = :sprintId order by b.sprint.sprintOrder ASC")
    Slice<Backlog> findBySprintIdDesc(@Param("sprintId") Long sprintId, Pageable pageable);

    @Query("SELECT b FROM Backlog b JOIN b.sprint s JOIN b.userBacklogs u WHERE s.project.projectId = :projectId AND u.user.userId = :userId order by b.sprint.sprintOrder ASC")
    Slice<Backlog> findByProjectIdDesc(@Param("projectId") Long projectId,@Param("userId") Long userId, Pageable pageable);

    Optional<Backlog> findByBacklogId(Long backlogId);

    boolean existsByBacklogId(Long backlogId);
}
