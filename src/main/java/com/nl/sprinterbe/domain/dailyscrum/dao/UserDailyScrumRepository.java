package com.nl.sprinterbe.domain.dailyscrum.dao;

import com.nl.sprinterbe.domain.dailyscrum.entity.UserDailyScrum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDailyScrumRepository extends JpaRepository<UserDailyScrum, Long> {

    @Query("SELECT ud FROM UserDailyScrum ud WHERE ud.dailyScrum.dailyScrumId = :dailyScrumId")
    List<UserDailyScrum> findByDailyScrumId(@Param("dailyScrumId") Long dailyScrumId);


    @Query("SELECT ud FROM UserDailyScrum ud WHERE ud.dailyScrum.dailyScrumId = :dailyScrumId AND ud.user.userId = :userId")
    Optional<UserDailyScrum> findByDailyScrumIdAndUserId(
            @Param("dailyScrumId") Long dailyScrumId,
            @Param("userId") Long userId);
}
