package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.dailyscrum.dto.DailyScrumResponse;
import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface DailyScrumRepository extends JpaRepository<DailyScrum, Long>, DailyScrumSearchQueryRepository {
    @Query("select ud.user from UserDailyScrum ud where ud.dailyScrum.dailyScrumId = :dailyScrumId")
    List<User> findUsersByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);

    @Query("select d FROM DailyScrum  d join d.sprint s where s.sprintId = :sprintId")
    List<DailyScrum> findDailyScrumBySprintId(@Param("sprintId") Long sprintId);

    @Query("SELECT ds FROM DailyScrum ds" +
            " WHERE ds.sprint.project.projectId = :projectId" +
            " AND ds.createdAt = :createdAt")
    List<DailyScrum> findDailyScrumByProjectIdAndCreatedAt(@Param("projectId") Long projectId, @Param("createdAt") LocalDate createdAt);

    List<DailyScrum> findByCreatedAt(LocalDate startDate);

    // 일단 살려는 주실게
    @Query("SELECT new com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse(b.backlogId, b.isFinished, b.weight) " +
            "FROM DailyScrum ds " +
            "JOIN ds.dailyScrumBacklogs dsb " +
            "JOIN dsb.backlog b " +
            "WHERE ds.dailyScrumId = :dailyScrumId")
    List<BacklogResponse> findBacklogByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);
}
