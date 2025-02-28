package com.nl.sprinterbe.domain.project.entity;

import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.userProject.entity.UserProject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "project_name")
    private String projectName;

    // 프로젝트 1 : n 스프린트
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sprint> sprints = new ArrayList<>();

    // 다대다 매핑 (유저,프로젝트)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    // 컬렉션 초기화 문제 때문에 빌더 재정의
    @Builder
    public Project(Long projectId, LocalDateTime createdAt, String projectName) {
        this.projectId = projectId;
        this.createdAt = createdAt;
        this.projectName = projectName;
        this.sprints = new ArrayList<>();
        this.userProjects = new ArrayList<>();
    }

    /**
     * 연관관계 편의 메서드
     * 프로젝트에 스프린트 추가
     * @param sprint
     */
    public void addSprint(Sprint sprint) {
        sprints.add(sprint);
        sprint.setProject(this);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", createdAt=" + createdAt +
                ", projectName='" + projectName + '\'' +
                ", sprints=" + sprints +
                ", userProjects=" + userProjects +
                '}';
    }
}
