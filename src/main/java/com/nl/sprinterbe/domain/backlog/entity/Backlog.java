package com.nl.sprinterbe.domain.backlog.entity;

import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.userBacklog.entity.UserBacklog;
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

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    private Sprint sprint;

    @Column(name = "assigned_user_id")
    private Long assingedUserId;

    @Column(name = "is_finish")
    private Boolean isFinish;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "backlog")
    private List<UserBacklog> userBacklogs = new ArrayList<>();
/*
    @OneToMany(mappedBy = "backlog")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<BacklogComment> backlogComments = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<Issue> issues = new ArrayList<>();*/

}