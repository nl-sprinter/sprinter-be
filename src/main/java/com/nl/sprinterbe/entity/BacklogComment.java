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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BacklogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backlog_comment_id")
    private Long backlogCommentId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "backlog_id", nullable = false)
    private Backlog backlog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "BacklogComment")
    private List<Like> likes = new ArrayList<>();

}
