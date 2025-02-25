package com.nl.sprinterbe.domain.userProject.entity;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "is_project_leader")
    private Boolean isProjectLeader;

    public UserProject(User user, Project project, Boolean isProjectLeader) {
        this.user = user;
        this.project = project;
        this.isProjectLeader = isProjectLeader;
    }
}
