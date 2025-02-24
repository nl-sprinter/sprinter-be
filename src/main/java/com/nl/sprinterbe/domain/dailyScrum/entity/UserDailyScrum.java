package com.nl.sprinterbe.domain.dailyScrum.entity;

import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDailyScrum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_dailyscrum_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dailyscrum_id", nullable=false)
    private DailyScrum dailyScrum;
}
