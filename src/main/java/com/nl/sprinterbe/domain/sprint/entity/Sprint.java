package com.nl.sprinterbe.domain.sprint.entity;

import com.nl.sprinterbe.domain.dailyScrum.entity.DailyScrum;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.sprint.dto.SprintDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long sprintId;

    @Column(name = "sprint_name")
    private String sprintName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "sprint_order")
    private Long sprintOrder;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public static Sprint createSprint(SprintDto sprintDto, Project project) {
        return Sprint.builder()
                .sprintName(sprintDto.getSprintName())
                .startDate(sprintDto.getStartDate())
                .endDate(sprintDto.getEndDate())
                .sprintOrder(sprintDto.getSprintOrder())
                .project(project)
                .build();
    }


//    @OneToMany(mappedBy = "sprint")
//    private List<Backlog> backlogs = new ArrayList<>();
//

}
