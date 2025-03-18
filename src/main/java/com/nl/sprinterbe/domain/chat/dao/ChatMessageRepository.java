package com.nl.sprinterbe.domain.chat.dao;

import com.nl.sprinterbe.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_IdOrderByTimestampAsc(Long chatRoomId);
    Page<ChatMessage> findByChatRoom_IdOrderByTimestampDesc(Long chatRoomId, Pageable pageable);
}