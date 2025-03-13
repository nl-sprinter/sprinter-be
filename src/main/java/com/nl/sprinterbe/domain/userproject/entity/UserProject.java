package com.nl.sprinterbe.domain.userproject.entity;

import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Override
    public String toString() {
        return "UserProject(id=" + id + 
               ", userId=" + (user != null ? user.getUserId() : null) + 
               ", projectId=" + (project != null ? project.getProjectId() : null) + 
               ", isProjectLeader=" + isProjectLeader + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProject that = (UserProject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
