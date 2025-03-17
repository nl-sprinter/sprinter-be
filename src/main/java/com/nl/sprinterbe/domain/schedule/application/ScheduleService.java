package com.nl.sprinterbe.domain.schedule.application;

import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleListResponse;
import com.nl.sprinterbe.domain.schedule.dto.ScheduleDto;
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
import com.nl.sprinterbe.global.security.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> getScheduleList(Long projectId, int year , int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();

        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(23, 59, 59, 999_999_999);

        List<Schedule> schedules = scheduleRepository.findAllScheduleInMonth(startOfMonth, endOfMonth, projectId);
        List<Sprint> sprints = sprintRepository.findAllSprintInMonth(startDate, lastDay, projectId);

        // 3. 각각을 DTO로 변환
        List<ScheduleListResponse> scheduleDtoList = schedules.stream()
                .map(ScheduleListResponse::of)
                .toList();

        List<ScheduleListResponse> sprintDtoList = sprints.stream()
                .map(ScheduleListResponse::of)
                .toList();

        // 4. 두 리스트를 합침
        List<ScheduleListResponse> result = new ArrayList<>();
        result.addAll(scheduleDtoList);
        result.addAll(sprintDtoList);
        return result;
    }

    public void createSchedule(ScheduleDto request, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<User> users = userRepository.findAllById(request.getUserId());

        Schedule schedule = Schedule.builder()
                .project(project)
                .title(request.getTitle())
                .startDateTime(request.getStartTime())
                .endDateTime(request.getEndTime())
                .isAllDay(request.getIsAllDay())
                .notify(request.getNotify())
                .preNotificationHours(request.getPreNotificationHours())
                .color(request.getColor())
                .build();

        schedule.setUsers(users);
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public void updateSchedule(ScheduleDto request, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        // 모든 필드를 무조건 setter로 업데이트 (요청에 null이 포함되면 기존 값이 null로 변경됨)
        schedule.setTitle(request.getTitle());
        schedule.setIsAllDay(request.getIsAllDay());
        schedule.setStartDateTime(request.getStartTime());
        schedule.setEndDateTime(request.getEndTime());
        schedule.setNotify(request.getNotify());
        schedule.setPreNotificationHours(request.getPreNotificationHours());
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

    public ScheduleDto getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        List<Long> userList = schedule.getUserSchedules().stream()
                .map(us -> us.getUser().getUserId())
                .toList();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .userId(userList)
                .title(schedule.getTitle())
                .isAllDay(schedule.getIsAllDay())
                .startTime(schedule.getStartDateTime())
                .endTime(schedule.getEndDateTime())
                .notify(schedule.getNotify())
                .preNotificationHours(schedule.getPreNotificationHours())
                .color(schedule.getColor())
                .build();

        return scheduleDto;
    }

}
