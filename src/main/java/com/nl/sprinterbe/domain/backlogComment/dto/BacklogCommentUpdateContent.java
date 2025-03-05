package com.nl.sprinterbe.domain.backlogComment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacklogCommentUpdateContent {
    @NotBlank(message = "content는 필수 값입니다.")
    private String content;
}
