package com.nl.sprinterbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Backlog {
    @Id
    @Column(name = "backlog_id")
    private Long backlogId;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "backlog")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<BacklogComment> backlogComments = new ArrayList<>();

    @OneToMany(mappedBy = "backlog")
    private List<Issue> issues = new ArrayList<>();

}
