package com.nl.sprinterbe.domain.issue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateIssueRequest {

    @NotNull(message = "backlogId는 필수값입니다.")
    private Long backlogId;

    @Length(max = 100, message = "content는 100자 이하여야 합니다.")
    private String content;

    private Boolean checked=false;
}
