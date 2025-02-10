package com.nl.sprinterbe.user.entity;
//import com.nl.sprinterbe.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;
    private String nickname;
    private String password;
    private String email;
    private String provider;
//    @OneToMany(mappedBy = "user")
//    private List<Todo> todos = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<UserProject> userProjects = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Schedule> schedules = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Notification> notifications = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Like> likes = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<UserBacklog> userBacklogs = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<BacklogComment> backlogComments = new ArrayList<>();


}
