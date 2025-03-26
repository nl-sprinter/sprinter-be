package com.nl.sprinterbe.domain.backlog.entity;

import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.userbacklog.entity.UserBacklog;
import com.nl.sprinterbe.domain.dailyscrum.entity.DailyScrumBacklog;
import com.nl.sprinterbe.domain.task.entity.Task;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @Setter
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

    // 다대다 매핑 (백로그, 유저)
    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserBacklog> userBacklogs = new ArrayList<>();

    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DailyScrumBacklog> dailyScrumBacklogs = new ArrayList<>();

    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

}