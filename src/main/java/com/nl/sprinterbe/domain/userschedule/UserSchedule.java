package com.nl.sprinterbe.domain.userschedule;

import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class UserSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    Schedule schedules;

    public UserSchedule(User users, Schedule schedules) {
        this.users = users;
        this.schedules = schedules;
    }
}
