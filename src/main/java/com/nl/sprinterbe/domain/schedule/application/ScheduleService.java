package com.nl.sprinterbe.domain.schedule.application;

import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.schedule.dto.MyScheduleResponse;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleRequest;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleResponse;
import com.nl.sprinterbe.domain.schedule.dao.ScheduleRepository;
import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.schedule.ScheduleNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.domain.userschedule.UserSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedule(Long projectId, int year , int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();

        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(23, 59, 59, 999_999_999);

        List<Schedule> schedules = scheduleRepository
                .findAllScheduleInMonth(startOfMonth, endOfMonth,projectId);
        // 2) Stream으로 DTO 변환
        return schedules.stream()
                .map(ScheduleResponse::of)     // Schedule -> ScheduleResponse
                .collect(Collectors.toList()); // List<ScheduleResponse>
    }

    @Transactional(readOnly = true)
    public List<MyScheduleResponse> getMySchedule(Long projectId, Long userId, int year , int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();

        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(23, 59, 59, 999_999_999);

        List<Schedule> schedules = scheduleRepository.findAllMyScheduleInMonthByUserId(startOfMonth, endOfMonth, projectId, userId);
        List<Sprint> sprints = sprintRepository.findAllSprintInMonthByUserId(startDate, lastDay, projectId, userId);
        // 3. 각각을 DTO로 변환
        List<MyScheduleResponse> scheduleDtos = schedules.stream()
                .map(MyScheduleResponse::of)
                .collect(Collectors.toList());

        List<MyScheduleResponse> sprintDtos = sprints.stream()
                .map(MyScheduleResponse::of)
                .collect(Collectors.toList());

        // 4. 두 리스트를 합침
        List<MyScheduleResponse> result = new ArrayList<>();
        result.addAll(scheduleDtos);
        result.addAll(sprintDtos);
        return result;
    }

    public void createSchedule(ScheduleRequest request, Long projectId) {
        // 1. Project 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        // 2. Schedule 생성 (요청 데이터를 기반으로)
        Schedule schedule = Schedule.builder()
                .project(project)
                .startDateTime(request.getStartTime())
                .endDateTime(request.getEndTime())
                .title(request.getTitle())
                .notify(request.getIsAlarmOn())
                .color(request.getColor())
                .preNotificationTime(request.getPreNotificationTime())
                .isAllDay(request.getIsAllDay())
                .build();

        // 3. 요청에 포함된 모든 userId에 대해 UserSchedule 연관관계 생성
        for (Long userId : request.getUserId()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);

            // 중간 테이블 엔티티 UserSchedule 생성 및 연관관계 설정
            UserSchedule userSchedule = new UserSchedule(user,schedule);

            // 양방향 연관관계라면 양쪽 컬렉션에 추가
            schedule.getUserSchedules().add(userSchedule);
            user.getUserSchedules().add(userSchedule);
        }

        // 4. Schedule 저장 (Cascade 옵션에 따라 UserSchedule도 함께 persist됨)
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public void updateSchedule(ScheduleRequest request, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        // 모든 필드를 무조건 setter로 업데이트 (요청에 null이 포함되면 기존 값이 null로 변경됨)
        schedule.setTitle(request.getTitle());
        schedule.setIsAllDay(request.getIsAllDay());
        schedule.setStartDateTime(request.getStartTime());
        schedule.setEndDateTime(request.getEndTime());
        schedule.setNotify(request.getIsAlarmOn());
        schedule.setPreNotificationTime(request.getPreNotificationTime());
        schedule.setColor(request.getColor());

        // 연관관계도 동일하게 처리
        // 기존 userSchedules 제거 및 재설정
        schedule.getUserSchedules().clear();
        if (request.getUserId() != null) {
            for (Long userId : request.getUserId()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(UserNotFoundException::new);
                UserSchedule userSchedule = new UserSchedule(user, schedule);
                schedule.getUserSchedules().add(userSchedule);
                user.getUserSchedules().add(userSchedule);
            }
        }
    }
}
