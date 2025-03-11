package com.nl.sprinterbe.domain.dailyScrum.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class DailyScrumBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="backlog_id", nullable=false)
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dailyscrum_id", nullable=false)
    private DailyScrum dailyScrum;

    public static DailyScrumBacklog of(DailyScrum dailyScrum, Backlog backlog) {
        DailyScrumBacklog dsb = new DailyScrumBacklog();
        dsb.dailyScrum = dailyScrum;
        dsb.backlog = backlog;
        return dsb;
    }
}
