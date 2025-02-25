package com.nl.sprinterbe.domain.issue.entity;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.issue.dto.CreateIssueRequest;
import com.nl.sprinterbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    private String content;

    private Boolean checked;

    // TODO: user랑 연관관계 매핑해야함

    @ManyToOne
    @JoinColumn(name = "backlog_id", nullable = false)
    private Backlog backlog;

    public static Issue of(CreateIssueRequest request){
        return Issue.builder()
                .content(request.getContent())
                .checked(request.getChecked())
                .build();
    }

    public void modifyContent(CreateIssueRequest request){
        this.content = request.getContent();
    }

    public void modifyChecked(CreateIssueRequest request){
        this.checked = request.getChecked();
    }
}
