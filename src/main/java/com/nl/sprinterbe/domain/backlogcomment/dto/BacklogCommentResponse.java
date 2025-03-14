package com.nl.sprinterbe.domain.backlogcomment.dto;

import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BacklogCommentResponse {

    private Long BacklogCommentId;
    private Long parentCommentId;

    private String nickname;
    private String content;
    private LocalDateTime createdDate;

    public static BacklogCommentResponse of(BacklogComment backlogComment) {
        return BacklogCommentResponse.builder()
                .BacklogCommentId(backlogComment.getBacklogCommentId())
                .content(backlogComment.getContent())
                .createdDate(backlogComment.getCreatedAt())
                .nickname(backlogComment.getUser().getNickname())
                .parentCommentId(backlogComment.getParentCommentId()) // TODO 널값 검사할지안할지
                .build();
    }
}
