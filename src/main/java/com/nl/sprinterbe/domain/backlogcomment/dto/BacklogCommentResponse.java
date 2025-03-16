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
    private Long userId;

    private String nickname;
    private String content;
    private LocalDateTime createdDate;

    private Long likeCount;

    public static BacklogCommentResponse of(BacklogComment backlogComment, Long likeCount) {
        return BacklogCommentResponse.builder()
                .BacklogCommentId(backlogComment.getBacklogCommentId())
                .parentCommentId(backlogComment.getParentCommentId())
                .userId(backlogComment.getUser().getUserId())
                .nickname(backlogComment.getUser().getNickname())
                .content(backlogComment.getContent())
                .createdDate(backlogComment.getCreatedAt())
                .likeCount(likeCount)
                .build();
    }
}
