package com.nl.sprinterbe.domain.user.entity;
//import com.nl.sprinterbe.entity.*;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Override
    public String toString() {
        return "User(userId=" + userId + 
               ", nickname=" + nickname + 
               ", email=" + email + 
               ", provider=" + provider + 
               ", role=" + role + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
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
