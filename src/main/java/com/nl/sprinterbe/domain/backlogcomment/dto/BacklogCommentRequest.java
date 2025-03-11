package com.nl.sprinterbe.domain.backlogcomment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacklogCommentRequest {

    /*
    * parentCommentId는 최상위 댓글의 경우 안받아도 됨*/
    private Long parentCommentId;

    @NotBlank(message = "content는 필수 값입니다.")
    private String content;

}
