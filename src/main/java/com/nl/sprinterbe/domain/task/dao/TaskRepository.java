package com.nl.sprinterbe.domain.task.dao;

import com.nl.sprinterbe.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, TaskSearchQueryRepository {

    @Query("SELECT t FROM Task t WHERE t.backlog.backlogId = :backlogId")
    List<Task> findByBacklogId(@Param("backlogId") Long backlogId);

    // 유저가 프로젝트를 나갈 때, 유저에게 할당되어 있는 task 의 userId 를 null 로 세팅
    @Modifying
    @Query("UPDATE Task t" +
            " SET t.userId = NULL" +
            " WHERE t.userId = :userId")
    void updateUserIdToNullWhenUserGoOutProject(@Param("userId") Long userId);
}
