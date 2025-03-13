package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrumBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyScrumBacklogRepository extends JpaRepository<DailyScrumBacklog, Long> {

    List<DailyScrumBacklog> findByDailyScrum(DailyScrum dailyScrum);

    @Query("select dsb from DailyScrumBacklog dsb where dsb.dailyScrum.dailyScrumId = :dailyScrumId")
    List<DailyScrumBacklog> findByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);

    @Query("SELECT dsb FROM DailyScrumBacklog dsb " +
            "WHERE dsb.dailyScrum.dailyScrumId = :dailyScrumId " +
            "AND dsb.backlog.backlogId = :backlogId")
    Optional<DailyScrumBacklog> findByDailyScrumAndBacklog(
            @Param("dailyScrumId") Long dailyScrumId,
            @Param("backlogId") Long backlogId
    );

    @Query("SELECT new com.nl.sprinterbe.domain.dailyscrum.dto.BacklogResponse(dsb.backlog.backlogId, dsb.backlog.isFinished, dsb.backlog.weight, dsb.backlog.title) " +
            "FROM DailyScrumBacklog dsb " +
            "WHERE dsb.dailyScrum.dailyScrumId = :dailyScrumId")
    List<BacklogResponse> findBacklogByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);


    @Modifying
    @Query("DELETE FROM DailyScrumBacklog dsb" +
            " WHERE dsb.backlog.backlogId = :backlogId" +
            " AND dsb.dailyScrum.dailyScrumId = :dailyScrumId")
    void deleteByDailyScrumDailyScrumIdAnd(@Param("dailyScrumId") Long dailyScrumId, @Param("backlogId") Long backlogId);
}
