package com.nl.sprinterbe.domain.sprint.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long sprintId;

    @Column(name = "sprint_name")
    private String sprintName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "sprint_order")
    private Long sprintOrder;

    // 빌더 재정의
    @Builder
    public Sprint(Long sprintId, String sprintName, LocalDate startDate,
                  LocalDate endDate, Long sprintOrder, Project project) {
        this.sprintId = sprintId;
        this.sprintName = sprintName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sprintOrder = sprintOrder;
        this.project = project;
        this.backlogs = new ArrayList<>();
    }


    // 프로젝트 1 : n 스프린트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
//
    // 스프린트 1 : n 백로그
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Backlog> backlogs = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addBacklog(Backlog backlog) {
        backlogs.add(backlog);
        backlog.setSprint(this);
    }

}
