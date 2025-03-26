package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.admin.dto.AlarmRequest;
import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.chat.dao.ChatMessageRepository;
import com.nl.sprinterbe.domain.notification.dao.NotificationRepository;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.notification.entity.UserNotification;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.schedule.dao.ScheduleRepository;
import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.schedule.ScheduleNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final ScheduleRepository scheduleRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 스케줄 알림 체크 주기 (1분)
    private static final long SCHEDULE_CHECK_INTERVAL = 30 * 1000;

    public void create(NotificationType notificationType, String content, Long projectId, String url, Long scheduleId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<User> users = projectRepository.findAllUsersByProjectId(projectId);

        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .content(content)
                .project(project)
                .createdAt(LocalDateTime.now())
                .navigable(url != null)
                .scheduleId(scheduleId)
                .url(url)
                .build();



        List<UserNotification> userNotifications = users.stream()
                .map(user -> UserNotification.builder()
                        .users(user)
                        .notification(notification)
                        .build())
                .toList();
        
        notification.setUserNotification(userNotifications);

        notificationRepository.save(notification);
    }


    public List<NotificationDto> findNotificationsByUserId(Long userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    public Map<String,Long> countNotificationByUserId(Long userId){
        Long count = notificationRepository.countNotificationByUserId(userId);
        return Map.of("count", count);
    }

    public void deleteNotificationByNotificationId(Long notificationId) {
        notificationRepository.deleteByNotificationId(notificationId);
    }

    public void deleteAllNotificationsByUserId(Long userId) {
        notificationRepository.deleteNotificationByUserId(userId);
    }

    public String makeCommentContent(Long senderId,Long backlogId) {
        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(BacklogNotFoundException::new);
        String senderName = sender.getNickname();
        return senderName + "님이\""  +backlog.getTitle() + "\" 백로그에 새로운 댓글을 남겼습니다.";
    }

    public String makeCommentUrl(Long projectId , Long sprintId , Long backlogId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/backlogs/" + backlogId;
    }

    public String makeIssueContent(Long senderId, Long backlogId) {
        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(BacklogNotFoundException::new);
        String senderName = sender.getNickname();
        return senderName + "님이\"" + backlog.getTitle() + "\" 백로그에 새로운 이슈를 등록했습니다.";
    }

    public String makeIssueUrl(Long projectId , Long sprintId , Long backlogId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/backlogs/" + backlogId;
    }

    public String makeDailyScrumContent(Long senderId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime + "에 데일리 스크럼이 예정되어 있습니다.";
    }

    public String makeDailyScrumUrl(Long projectId , Long sprintId , Long dailyScrumId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/dailyScrums/" + dailyScrumId;
    }

    public String makeTeammateContent(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getNickname() + "님이 프로젝트에 참여했습니다.";
    }

    // 스케줄 알림 시간 전에 Request 없이 해야됨.
    public String makeScheduleContent(Long scheduleId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDateTime = now.format(formatter);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        return formattedDateTime + "에 \""+ schedule.getTitle() +"\"가 예정되어 있습니다.";
    }

    public String makeScheduleUrl(Long projectId ,Long scheduleId){
        return "/projects/" + projectId + "/calendar/schedule/" + scheduleId;
    }


    public void sendAlarmToUsers(AlarmRequest request) {
        // 유저 ID 목록으로 유저 조회
        List<User> users = userRepository.findAllById(request.getUserId());

        if (users.isEmpty()) {
            return; // 유저가 없으면 아무 작업도 하지 않음
        }

        // 각 유저에게 알림 전송
        for (User user : users) {
            // 관리자 알림 타입으로 알림 생성
            // 프로젝트 ID는 null 또는 관리자 프로젝트 ID 사용
            // URL은 null 또는 관리자 페이지 URL 사용
            createAdminNotification(
                    NotificationType.ADMIN_NOTICE,
                    request.getContent(),
                    user.getUserId()
            );
        }
    }

    public void createAdminNotification(NotificationType notificationType, String content, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .content(content)
                .createdAt(LocalDateTime.now())
                .navigable(false)  // 관리자 알림은 이동할 URL이 없음
                .url(null)
                .build();

        // 단일 사용자에게만 알림 전송
        List<UserNotification> userNotifications = List.of(
                UserNotification.builder()
                        .users(user)
                        .notification(notification)
                        .build()
        );

        notification.setUserNotification(userNotifications);
        notificationRepository.save(notification);
    }

    // 채팅 시 알람
    public String makeChattingContent(Long senderId, Long chattingId) {
        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        String senderName = sender.getNickname();

        return senderName + "님이 새로운 메시지를 보냈습니다: \"" + chatMessageRepository.findById(chattingId);
    }

    //chatting URL
    public String makeChattingUrl(Long projectId , Long sprintId , Long backlogId){
        return "/projects/" +projectId;
    }

    // 스케줄 알림 체크 및 DB에 저장 (스케줄러로 주기적 실행)
    @Scheduled(fixedRate = SCHEDULE_CHECK_INTERVAL)
    @Transactional
    public void checkScheduleNotifications() {

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        
        // 알림 설정된 모든 스케줄 조회 (notify=true)
        List<Schedule> schedules = scheduleRepository.findAllByNotifyTrue();
        
        for (Schedule schedule : schedules) {
            // 알림 시간 계산 (시작 시간 - 사전 알림 시간)
            LocalDateTime notificationTime = schedule.getStartDateTime()
                    .minusHours(schedule.getPreNotificationHours());
            
            // 현재 시간이 알림 시간 이후이고, 시작 시간 이전인 경우에만 알림 생성
            if (now.isAfter(notificationTime) && now.isBefore(schedule.getStartDateTime())) {
                // 이미 알림을 보냈는지 확인 (중복 방지)
                boolean alreadySent = notificationRepository.existsByScheduleIdAndNotificationType(
                        schedule.getScheduleId(), NotificationType.SCHEDULE);
                
                if (!alreadySent) {
                    // 알림 생성 및 저장
                    String content = makeScheduleContent(schedule.getScheduleId());
                    String url = makeScheduleUrl(schedule.getProject().getProjectId(), schedule.getScheduleId());
                    
                    // 프로젝트의 모든 사용자에게 알림 생성
                    create(NotificationType.SCHEDULE,makeScheduleContent(schedule.getScheduleId()),schedule.getProject().getProjectId(),makeScheduleUrl(schedule.getProject().getProjectId(),schedule.getScheduleId()),schedule.getScheduleId());
                }
            }
        }
    }
}
