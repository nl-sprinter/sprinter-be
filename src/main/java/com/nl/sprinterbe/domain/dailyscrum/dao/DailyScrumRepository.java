package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface DailyScrumRepository extends JpaRepository<DailyScrum, Long> {
    @Query("select ud.user from UserDailyScrum ud where ud.dailyScrum.dailyScrumId = :dailyScrumId")
    List<User> findUsersByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);

    @Query("select d FROM DailyScrum  d join d.sprint s where s.sprintId = :sprintId")
    List<DailyScrum> findDailyScrumBySprintId(@Param("sprintId") Long sprintId);

    List<DailyScrum> findByCreatedAt(LocalDate startDate);
}
