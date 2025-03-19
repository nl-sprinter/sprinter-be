package com.nl.sprinterbe.domain.notification.entity;

import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNotificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public UserNotification() {}
}
