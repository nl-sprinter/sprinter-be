package com.nl.sprinterbe.domain.backlog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BacklogUserUpdateRequest {
    // 추가할 유저의 ID 목록 (없으면 null 또는 빈 리스트)
    private List<Long> addUserIds;
    // 삭제할 유저의 ID 목록 (없으면 null 또는 빈 리스트)
    private List<Long> removeUserIds;
}
