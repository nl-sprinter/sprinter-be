package com.nl.sprinterbe.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "project_name")
    private String projectName;

//    @OneToMany(mappedBy = "project")
//    private List<Sprint> sprints = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Todo> todos = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Notification> notifications = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Schedule> schedules = new ArrayList<>();
}
