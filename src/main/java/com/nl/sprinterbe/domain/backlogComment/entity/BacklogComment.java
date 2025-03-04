package com.nl.sprinterbe.domain.backlogComment.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BacklogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backlog_comment_id")
    private Long backlogCommentId;

    private String content;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "backlog_id", nullable = false)
    private Backlog backlog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private BacklogComment parentComment;

    @OneToMany(
            mappedBy = "parentComment",
            fetch = FetchType.LAZY)
    private List<BacklogComment> childComments = new ArrayList<>();

    public void setParent(BacklogComment parent) {
        this.parentComment = parent;
        parent.getChildComments().add(this);
    }

    public static BacklogComment of(BacklogCommentRequest request, Backlog backlog, Optional<User> user) {
        BacklogComment newComment = BacklogComment.builder()
                .backlog(backlog)
                .user(user.get())
                .content(request.getContent())
                .build();
        return newComment;
    }

}
