package com.nl.sprinterbe.domain.schedule.dao;

import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {


    //해당 년, 월에 걸려있는 Schedule 모두 가져오기
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.project.projectId = :projectId " +
            "  AND s.startDateTime <= :endOfMonth " +
            "  AND s.endDateTime >= :startOfMonth")
    List<Schedule> findAllScheduleInMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                          @Param("endOfMonth") LocalDateTime endOfMonth ,
                                          @Param("projectId") Long projectId);

    //    @Query("SELECT s FROM Schedule s " +
//            "WHERE s.project.projectId = :projectId " +
//            "  AND s.user.userId=:userId" +
//            "  AND s.startDateTime <= :endOfMonth " +
//            "  AND s.endDateTime >= :startOfMonth")
    @Query("SELECT s FROM Schedule s JOIN s.userSchedules us WHERE s.project.projectId=:projectId AND us.users.userId=:userId AND s.startDateTime <= :endOfMonth AND s.endDateTime >= :startOfMonth")
    List<Schedule> findAllMyScheduleInMonthByUserId(@Param("startOfMonth") LocalDateTime startOfMonth,
                                                    @Param("endOfMonth") LocalDateTime endOfMonth ,
                                                    @Param("projectId") Long projectId , @Param("userId") Long userId);

}
