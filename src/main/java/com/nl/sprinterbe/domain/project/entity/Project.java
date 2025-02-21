package com.nl.sprinterbe.domain.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

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
    private Date createdAt;

    @Column(name = "project_name")
    private String projectName;

//    @OneToMany(mappedBy = "project")
//    private List<Todo> todos = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Notification> notifications = new ArrayList<>();
//
//    @OneToMany(mappedBy = "project")
//    private List<Schedule> schedules = new ArrayList<>();
}
