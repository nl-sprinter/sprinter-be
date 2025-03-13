package com.nl.sprinterbe.domain.dailyscrum.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.jpa.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DailyScrum extends JpaBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_scrum_id")
    private Long dailyScrumId;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    private Sprint sprint;

    @OneToMany(mappedBy = "dailyScrum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyScrumBacklog> dailyScrumBacklogs = new ArrayList<>();

    @OneToMany(mappedBy = "dailyScrum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDailyScrum> userDailyScrums = new ArrayList<>();

    private String title;

    private String content;

    // 연관관계 편의 메서드
    public void addUser(User user) {
        this.userDailyScrums.add(UserDailyScrum.builder().dailyScrum(this).user(user).build());
    }

    // 편의 메서드
    public void addBacklog(Backlog backlog) {
        this.dailyScrumBacklogs.add(DailyScrumBacklog.builder().dailyScrum(this).backlog(backlog).build());
    }
}
