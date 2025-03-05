package com.nl.sprinterbe.domain.backlog.entity;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Backlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backlog_id")
    private Long backlogId;

    @Column(name = "is_finish")
    private Boolean isFinished;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "title")
    @Setter
    private String title;

    // 스프린트 1 : n 백로그
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Setter
    private Sprint sprint;

/*    @OneToMany(mappedBy = "backlog")
    private List<UserBacklog> userBacklogs = new ArrayList<>();*/

//    @ManyToOne
//    @JoinColumn(name= "daily_scrum_id")
//    @Setter
//    private DailyScrum dailyScrum;

}