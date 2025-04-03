package com.nl.sprinterbe.domain.freespeech.dto;

import com.nl.sprinterbe.domain.freespeech.entity.FreeSpeech;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FreeSpeechDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    public static FreeSpeechDto of(FreeSpeech freeSpeech) {
        return new FreeSpeechDto(freeSpeech.getSpeechId(), freeSpeech.getContent(), freeSpeech.getCreatedAt());
    }

}
