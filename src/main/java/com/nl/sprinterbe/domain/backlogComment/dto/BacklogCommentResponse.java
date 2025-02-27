package com.nl.sprinterbe.domain.backlogComment.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nl.sprinterbe.domain.backlogComment.entity.BacklogComment;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"backlogCommentId", "content", "nickname", "parentCommentId", "createdDate", "childComments"})
public class BacklogCommentResponse {
    private Long BacklogCommentId;
    private String content;
    private LocalDate createdDate;
    private String nickname;
    private Long parentCommentId;
    private List<BacklogCommentResponse> childComments;

    public static BacklogCommentResponse of(BacklogComment backlogComment) {
        return BacklogCommentResponse.builder()
                .BacklogCommentId(backlogComment.getBacklogCommentId())
                .content(backlogComment.getContent())
                .createdDate(backlogComment.getCreatedAt())
                .nickname(backlogComment.getUser().getNickname())
                .parentCommentId(backlogComment.getParentComment() == null ? -999L : backlogComment.getParentComment().getBacklogCommentId())
                .childComments(new ArrayList<>())
                .build();
    }
}
