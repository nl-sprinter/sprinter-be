package com.nl.sprinterbe.domain.user.entity;

import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import com.nl.sprinterbe.domain.dailyscrum.entity.UserDailyScrum;
import com.nl.sprinterbe.domain.userbacklog.entity.UserBacklog;
import com.nl.sprinterbe.domain.userproject.entity.UserProject;
import com.nl.sprinterbe.userschedule.UserSchedule;
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

    // 일대다 매핑 (유저, 백로그커맨트)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BacklogComment> backlogComments = new ArrayList<>();

    // 다대다 매핑 (유저, 프로젝트)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    // 다대다 매핑 (유저 , 스케줄)
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserSchedule> userSchedules = new ArrayList<>();

    // 다대다 매핑 (유저, 백로그)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBacklog> userBacklogs = new ArrayList<>();

    // 다대다 매핑 (유저, 데일리스크럼)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDailyScrum> userDailyScrums = new ArrayList<>();




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

}
