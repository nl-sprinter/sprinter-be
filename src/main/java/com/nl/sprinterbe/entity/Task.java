//package com.nl.sprinterbe.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//public class Task {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "task_id")
//    private Long taskId;
//
//    @ManyToOne
//    @JoinColumn(name = "backlog_id", nullable = false)
//    private Backlog backlog;
//
//    private String content;
//
//}
