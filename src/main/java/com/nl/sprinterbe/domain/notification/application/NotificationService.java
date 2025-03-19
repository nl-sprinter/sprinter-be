package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.notification.dao.NotificationRepository;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.notification.entity.UserNotification;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;

    public void create(NotificationType notificationType, String content, Long projectId, String url) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<User> users = projectRepository.findAllUsersByProjectId(projectId);

        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .content(content)
                .project(project)
                .createdAt(LocalDateTime.now())
                .navigable(url != null)
                .url(url)
                .build();

        notification.setUserNotification(
                users.stream()
                        .map(user -> UserNotification.builder().users(user).notification(notification).build())
                        .toList() // List<UserNotification>으로 변환
        );


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
}
