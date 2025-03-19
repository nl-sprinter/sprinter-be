package com.nl.sprinterbe.domain.chat.application;

import com.nl.sprinterbe.domain.chat.dao.ChatMessageRepository;
import com.nl.sprinterbe.domain.chat.dao.ChatRoomRepository;
import com.nl.sprinterbe.domain.chat.dto.ChatMessageDto;
import com.nl.sprinterbe.domain.chat.entity.ChatMessage;
import com.nl.sprinterbe.domain.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ChatRoom getOrCreateChatRoom(Long projectId) {
        return chatRoomRepository.findByProjectId(projectId)
                .orElseGet(() -> {
                    ChatRoom newChatRoom = new ChatRoom();
                    newChatRoom.setProjectId(projectId);
                    return chatRoomRepository.save(newChatRoom);
                });
    }

    @Transactional
    public void saveMessage(ChatMessageDto messageDto) {
        try {
            ChatRoom chatRoom = getOrCreateChatRoom(messageDto.getProjectId());
            
            ChatMessage chatMessageEntity = ChatMessage.builder()
                .chatRoom(chatRoom)
                .userId(messageDto.getUserId())
                .nickname(messageDto.getNickname())
                .content(messageDto.getContent())
                .timestamp(LocalDateTime.parse(messageDto.getTimeStamp(), DATE_TIME_FORMATTER))
                .build();
                
            chatMessageRepository.save(chatMessageEntity);
        } catch (Exception e) {
            throw new RuntimeException("메시지 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatHistory(Long projectId) {
        ChatRoom chatRoom = getOrCreateChatRoom(projectId);
        
        return chatMessageRepository.findByChatRoom_IdOrderByTimestampAsc(chatRoom.getId())
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatHistoryPaginated(Long projectId, int page, int size) {
        ChatRoom chatRoom = getOrCreateChatRoom(projectId);
        Pageable pageable = PageRequest.of(page, size);
        
        return chatMessageRepository.findByChatRoom_IdOrderByTimestampDesc(chatRoom.getId(), pageable)
            .map(this::convertToDto);
    }
    
    private ChatMessageDto convertToDto(ChatMessage entity) {
        return ChatMessageDto.builder()
            .projectId(entity.getChatRoom().getProjectId())
            .userId(entity.getUserId())
            .nickname(entity.getNickname())
            .content(entity.getContent())
            .timeStamp(entity.getTimestamp().format(DATE_TIME_FORMATTER))
            .build();
    }
}
