package com.nl.sprinterbe.domain.backlogcomment.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.jpa.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "backlog_id", nullable = false)
    private Backlog backlog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long parentCommentId;

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
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
