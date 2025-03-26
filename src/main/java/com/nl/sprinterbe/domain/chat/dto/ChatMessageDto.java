package com.nl.sprinterbe.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ChatMessageDto {

    private Long projectId;
    private Long userId;
    private String nickname;
    private String content;
    private String timeStamp;
}
