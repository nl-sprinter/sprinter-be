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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Setter
    private Sprint sprint;

    //25.02.22 이거 왜 있는거였지?
    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Column(name = "is_finish")
    private Boolean isFinished;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "title")
    @Setter
    private String title;

/*    @OneToMany(mappedBy = "backlog")
    private List<UserBacklog> userBacklogs = new ArrayList<>();*/

    @ManyToOne
    @JoinColumn(name= "daily_scrum_id")
    @Setter
    private DailyScrum dailyScrum;


    /*
    @OneToMany(mappedBy = "backlog")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<BacklogComment> backlogComments = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<Issue> issues = new ArrayList<>();*/

}