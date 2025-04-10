package com.nl.sprinterbe.domain.notification.dao;

import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT new com.nl.sprinterbe.domain.notification.dto.NotificationDto(n.notificationId ,n.notificationType,n.content,n.createdAt,n.navigable,n.url,p.projectId,p.projectName) FROM UserNotification un JOIN un.notification n LEFT JOIN n.project p WHERE un.users.userId=:userId")
    List<NotificationDto> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(un) FROM UserNotification un WHERE un.users.userId=:userId")
    Long countNotificationByUserId(@Param("userId") Long userId);

    void deleteByNotificationId(Long notificationId);

    @Modifying
    @Query("DELETE FROM UserNotification un WHERE un.users.userId = :userId")
    void deleteNotificationByUserId(@Param("userId") Long userId);

    boolean existsByScheduleIdAndNotificationType(Long scheduleId, NotificationType notificationType);
}
