package com.nl.sprinterbe.domain.backlogcomment.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"backlogCommentId", "content", "nickname", "parentCommentId", "createdDate", "childComments"})
public class BacklogCommentFromResponse {
    private Long BacklogCommentId;
    private String content;
    private LocalDateTime createdDate;
    private String nickname;
    private Long parentCommentId;
    private List<BacklogCommentFromResponse> childComments;

    public static BacklogCommentFromResponse of(BacklogComment backlogComment) {
        return BacklogCommentFromResponse.builder()
                .BacklogCommentId(backlogComment.getBacklogCommentId())
                .content(backlogComment.getContent())
                .createdDate(backlogComment.getCreatedAt())
                .nickname(backlogComment.getUser().getNickname())
                .parentCommentId(backlogComment.getParentCommentId() == null ? 0 : backlogComment.getParentCommentId())
                .childComments(new ArrayList<>())
                .build();
    }
}
