package com.nl.sprinterbe.domain.dailyScrum.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.global.jpa.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<Backlog> backlogs = new ArrayList<>();


    private LocalDateTime startDate;

    private String title;

    private String content;

    // 편의 메서드
    public void addBacklog(Backlog backlog) {
        backlogs.add(backlog);
        backlog.setDailyScrum(this);
    }

}
