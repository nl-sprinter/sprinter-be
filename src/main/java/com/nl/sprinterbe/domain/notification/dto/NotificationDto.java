package com.nl.sprinterbe.domain.notification.dto;

import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private NotificationType notificationType;
    private String content;
    private LocalDateTime time;
    private Boolean navigable;
    private String url;
    private Long projectId;
    private String projectName;


}
