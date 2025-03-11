package com.nl.sprinterbe.domain.user.entity;
//import com.nl.sprinterbe.entity.*;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
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
    @Column(name = "user_id")
    private Long userId;

    private String nickname;

    private String password;

    private String email;

    private String provider;

    private String role;

    // 다대다 매핑 (유저, 프로젝트)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();


    //해당 유저가 프로젝트의 Leader인지 확인
    public boolean isProjectLeader(Long projectId) {
        return this.getUserProjects().stream()
                .filter(up -> up.getProject().getProjectId().equals(projectId))
                .anyMatch(UserProject::getIsProjectLeader);
    }


//    @OneToMany(mappedBy = "user")
//    private List<Todo> todos = new ArrayList<>();
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
