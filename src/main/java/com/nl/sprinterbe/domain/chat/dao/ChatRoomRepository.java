package com.nl.sprinterbe.domain.chat.dao;

import com.nl.sprinterbe.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByProjectId(Long projectId);
}
