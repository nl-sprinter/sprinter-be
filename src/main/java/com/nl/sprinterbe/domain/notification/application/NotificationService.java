package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.admin.dto.AlarmRequest;
import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.chat.dao.ChatMessageRepository;
import com.nl.sprinterbe.domain.chat.entity.ChatMessage;
import com.nl.sprinterbe.domain.notification.dao.NotificationRepository;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.notification.entity.UserNotification;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final ChatMessageRepository chatMessageRepository;


    public void createNotification(NotificationType notificationType, String content, Long projectId, String url, Long scheduleId) {
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

    public void sendAlarmToUsers(AlarmRequest request) {
        // 유저 ID 목록으로 유저 조회
        List<User> users = userRepository.findAllById(request.getUserIds());

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
                    request.getMessage(),
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
        ChatMessage chatMessage = chatMessageRepository.findById(chattingId).orElseThrow(UserNotFoundException::new);

        return senderName + "님이 새로운 메시지를 보냈습니다: \"" + chatMessage.getContent() + "\"";
    }
}
