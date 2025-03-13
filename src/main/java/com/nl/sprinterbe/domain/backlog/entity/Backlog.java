package com.nl.sprinterbe.domain.backlog.entity;

import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "title")
    @Setter
    private String title;

    @Column(name = "weight")
    @Setter
    private Long weight;

    @Column(name = "is_finish")
    private Boolean isFinished;



    // 스프린트 1 : n 백로그
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Setter
    private Sprint sprint;

    @OneToMany(mappedBy = "backlog", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Issue> issue;

    @OneToMany(mappedBy = "backlog", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BacklogComment> backlogComments;
/*    @OneToMany(mappedBy = "backlog")
    private List<UserBacklog> userBacklogs = new ArrayList<>();*/

//    @ManyToOne
//    @JoinColumn(name= "daily_scrum_id")
//    @Setter
//    private DailyScrum dailyScrum;

}