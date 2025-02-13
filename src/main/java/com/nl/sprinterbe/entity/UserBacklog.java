//package com.nl.sprinterbe.entity;
//
//import com.nl.sprinterbe.entity.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class UserBacklog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_backlog_id")
//    private Long userBacklogId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "backlog_id", nullable = false)
//    private Backlog backlog;
//
//
//
//}
