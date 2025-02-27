package com.nl.sprinterbe.domain.backlogComment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacklogCommentRequest {

    private Long parentCommentId;

    @NotBlank(message = "content는 필수 값입니다.")
    private String content;

}
