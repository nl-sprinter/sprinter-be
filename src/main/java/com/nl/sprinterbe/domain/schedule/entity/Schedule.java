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
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "schedules", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<UserSchedule> userSchedules = new ArrayList<>();

    @Column(name = "start_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_time")
    private LocalDateTime endDateTime;

    private String title;

    private Boolean notify;

    private Boolean isAllDay;

    @Enumerated(EnumType.STRING)
    private ScheduleColor color;

    private Integer preNotificationTime;

}
