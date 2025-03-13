package com.nl.sprinterbe.domain.task.dao;

import com.nl.sprinterbe.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.backlog.backlogId = :backlogId")
    List<Task> findByBacklogId(@Param("backlogId") Long backlogId);

    List<Task> findByUserId(Long userId);
}
