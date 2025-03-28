package com.nl.sprinterbe.domain.schedule.entity;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userschedule.UserSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String title;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList<>();

    @Column(name = "start_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_time")
    private LocalDateTime endDateTime;

    private Boolean isAllDay;

    private Boolean notify;

    private Integer preNotificationHours;

    @Enumerated(EnumType.STRING)
    private ScheduleColor color;


    // 연관관계 편의메서드
    public void setUsers(List<User> users) {
        this.userSchedules.clear();
        for (User user : users) {
            UserSchedule userSchedule = new UserSchedule(user, this);
            this.userSchedules.add(userSchedule);
            user.getUserSchedules().add(userSchedule);
        }
    }

    @Builder
    public Schedule(Long scheduleId, Project project, String title, LocalDateTime startDateTime,
                    LocalDateTime endDateTime, Boolean isAllDay, Boolean notify,
                    Integer preNotificationHours, ScheduleColor color) {
        this.scheduleId = scheduleId;
        this.project = project;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isAllDay = isAllDay;
        this.notify = notify;
        this.preNotificationHours = preNotificationHours;
        this.color = color;
        this.userSchedules = new ArrayList<>();
    }
}
