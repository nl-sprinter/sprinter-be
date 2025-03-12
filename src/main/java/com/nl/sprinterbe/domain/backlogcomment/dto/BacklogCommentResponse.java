package com.nl.sprinterbe.domain.backlogcomment.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BacklogCommentResponse {
    private Long BacklogCommentId;
    private String content;
    private LocalDateTime createdDate;
    private String nickname;
    private Long parentCommentId;

    public static BacklogCommentResponse of(BacklogComment backlogComment) {
        return BacklogCommentResponse.builder()
                .BacklogCommentId(backlogComment.getBacklogCommentId())
                .content(backlogComment.getContent())
                .createdDate(backlogComment.getCreatedAt())
                .nickname(backlogComment.getUser().getNickname())
                .parentCommentId(backlogComment.getParentComment() == null ? 0 : backlogComment.getParentComment().getBacklogCommentId())
                .build();
    }
}
