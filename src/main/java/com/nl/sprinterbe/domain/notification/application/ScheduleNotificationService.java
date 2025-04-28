package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleNotificationService {
    private final NotificationService notificationService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> scheduledTaskMap = new ConcurrentHashMap<>();

    @PreDestroy
    public void cleanup() {
        scheduler.shutdown();
    }

    public void scheduleNotification(Schedule schedule) {
        // 실제 알림이 갈 시간
        LocalDateTime notificationTime = schedule.getStartDateTime()
                .minusHours(schedule.getPreNotificationHours());

        // 이미 지난 시간이면 쳐냄
        if (notificationTime.isBefore(LocalDateTime.now())) {
            return;
        }

        // 스케줄러에 집어넣기
        ScheduledFuture<?> scheduledTask = scheduler.schedule(
                () -> sendScheduleNotification(schedule),
                Duration.between(LocalDateTime.now(), notificationTime).toMillis(),
                TimeUnit.MILLISECONDS
        );

        scheduledTaskMap.put(schedule.getScheduleId(), scheduledTask);
        log.info("스케줄 알림이 schedule 되었습니다. scheduleId={}", schedule.getScheduleId());
    }

    @Transactional
    public void sendScheduleNotification(Schedule schedule) {
        Long projectId = schedule.getProject().getProjectId();
        Long scheduleId = schedule.getScheduleId();
        notificationService.createNotification(
                NotificationType.SCHEDULE,
                makeScheduleContent(schedule),
                projectId,
                makeScheduleUrl(projectId, scheduleId),
                scheduleId
        );

        scheduledTaskMap.remove(scheduleId);
    }

    public void cancelScheduleNotification(Long scheduleId) {
        ScheduledFuture<?> removedScheduledTask = scheduledTaskMap.remove(scheduleId);
        if (removedScheduledTask != null) {
            removedScheduledTask.cancel(false);
        }
    }

    // 스케줄 알림 시간 전에 Request 없이 해야됨.
    public String makeScheduleContent(Schedule schedule) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDateTime = schedule.getStartDateTime().format(formatter);
        return formattedDateTime + "에 \""+ schedule.getTitle() +"\"이(가) 예정되어 있습니다.";
    }

    public String makeScheduleUrl(Long projectId ,Long scheduleId){
        return "/projects/" + projectId + "/calendar/schedule/" + scheduleId;
    }

}
