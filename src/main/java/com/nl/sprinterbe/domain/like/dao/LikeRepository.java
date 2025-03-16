package com.nl.sprinterbe.domain.like.dao;

import com.nl.sprinterbe.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("SELECT l.backlogComment.backlogCommentId, COUNT(l) FROM Like l " +
            "WHERE l.backlogComment.backlogCommentId IN :backlogCommentIds " +
            "GROUP BY l.backlogComment.backlogCommentId")
    List<Object[]> countLikesByBacklogCommentIds(@Param("backlogCommentIds") List<Long> backlogCommentIds);

    void deleteByBacklogCommentBacklogCommentIdAndUserUserId(Long backlogCommentId, Long userId);


    boolean existsByUserUserIdAndBacklogCommentBacklogCommentId(Long userId, Long backlogCommentId);

}
