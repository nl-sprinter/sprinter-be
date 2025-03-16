package com.nl.sprinterbe.userschedule;

import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    User users;

    @ManyToOne(fetch = FetchType.LAZY)
    Schedule schedules;

}
