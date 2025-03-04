package com.nl.sprinterbe.domain.dailyScrum.dao;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrumBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyScrumBacklogRepository extends JpaRepository<DailyScrumBacklog, Long> {

    List<DailyScrumBacklog> findByDailyScrum(DailyScrum dailyScrum);

    @Query("select dsb from DailyScrumBacklog dsb where dsb.dailyScrum.dailyScrumId = :dailyScrumId")
    List<DailyScrumBacklog> findByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);

    Optional<DailyScrumBacklog> findByDailyScrumIdAndBacklogId(Long dailyScrumId, Long backlogId);
}
