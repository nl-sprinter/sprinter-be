package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.notification.dao.NotificationRepository;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

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
