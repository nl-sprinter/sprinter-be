//package com.nl.sprinterbe.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//public class DailyScrum {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "daily_scrum_id")
//    private Long dailyScrumId;
//
//    @ManyToOne
//    @JoinColumn(name = "sprint_id", nullable = false)
//    private Sprint sprint;
//
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    private String content;
//
//}
