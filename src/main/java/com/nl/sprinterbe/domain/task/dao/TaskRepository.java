package com.nl.sprinterbe.domain.task.dao;

import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.todo.dto.TodoResponse;
import com.nl.sprinterbe.domain.todo.entity.TodoType;
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



    @Query("SELECT new com.nl.sprinterbe.domain.todo.dto.TodoResponse( " +
            "CAST('TASK' AS com.nl.sprinterbe.domain.todo.entity.TodoType)," +
            "t.content, " +
            "p.projectId, p.projectName, " +
            "s.sprintId, b.backlogId) " +
            "FROM Task t " +
            "JOIN t.backlog b " +  // Task → Backlog
            "JOIN b.sprint s " +   // Backlog → Sprint
            "JOIN s.project p " +  // Sprint → Project
            "WHERE t.userId = :userId " +
            "AND t.checked = false")
    List<TodoResponse> findUncheckedTasksByUserId(@Param("userId") Long userId);


    @Query("SELECT COUNT(t) FROM Task t WHERE t.userId = :userId AND t.checked = false")
    int countTasksTodoByUserIdAndUnchecked(@Param("userId") Long userId);

}
