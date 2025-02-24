package com.nl.sprinterbe.domain.project.entity;

import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "project_name")
    private String projectName;

    // Project 1 : n Sprint
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sprint> sprints = new ArrayList<>();

    /**
     * 프로젝트 생성 시, 스프린트들을 자동으로 생성하는 메서드
     */
    public static Project createProject(String projectName, int sprintCount, int sprintDuration) {
        Project project = new Project();
        project.projectName = projectName;
        project.createdAt = LocalDateTime.now();

        // 스프린트 자동 생성
        LocalDate startDate = LocalDate.now();
        for (int i = 0; i < sprintCount; i++) {
            String sprintName = "Sprint " + (i + 1);
            LocalDate endDate = startDate.plusDays(sprintDuration);
            SprintDto sprintDto = new SprintDto(
                    sprintName,
                    startDate,
                    endDate,
                    (long) i
            );
            Sprint sprint = Sprint.createSprint(sprintDto, project);
            project.sprints.add(sprint);
            startDate = endDate; // 다음 스프린트 시작 날짜 업데이트
        }
        return project;
    }

//
//    @OneToMany(mappedBy = "project")
//    private List<Notification> notifications = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Schedule> schedules = new ArrayList<>();
}
