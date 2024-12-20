package com.nl.sprinterbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "project_name")
    private String projectName;

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjects = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Schedule> schedules = new ArrayList<>();
}
