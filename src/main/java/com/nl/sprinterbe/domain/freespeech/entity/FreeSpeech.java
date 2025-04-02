package com.nl.sprinterbe.domain.freespeech.entity;

import com.nl.sprinterbe.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FreeSpeech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speechId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;

}
