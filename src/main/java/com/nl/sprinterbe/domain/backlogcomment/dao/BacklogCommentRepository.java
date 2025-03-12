package com.nl.sprinterbe.domain.backlogcomment.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BacklogCommentRepository extends JpaRepository<BacklogComment, Long> {

    @Query("SELECT bc FROM BacklogComment bc WHERE bc.backlog.backlogId = ?1")
    Backlog findByBacklogBacklogId(@Param("backlogId")Long backlogId);

    List<BacklogComment> findByUserUserId(Long backlogId);

//    List<BacklogComment> findByBacklogBacklogId(Long backlogId);

    @Query("SELECT bc FROM BacklogComment bc WHERE bc.backlog.backlogId = :backlogId")
    List<BacklogComment> findCommentsByBacklogId(@Param("backlogId") Long backlogId);
}
