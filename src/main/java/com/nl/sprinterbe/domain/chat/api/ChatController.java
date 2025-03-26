package com.nl.sprinterbe.domain.chat.api;

import com.nl.sprinterbe.domain.chat.application.ChatRoomService;
import com.nl.sprinterbe.domain.chat.dto.ChatMessageDto;
import com.nl.sprinterbe.domain.notification.application.NotificationService;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final NotificationService notificationService;

    @Operation(summary = "웹소켓으로 메세지 전송", description = "웹소켓을 사용하여 메세지를 전송합니다.")
    @MessageMapping("/room/{projectId}")
    @SendTo("/topic/room/{projectId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long projectId, ChatMessageDto message) { // 프론트 연동 OK
        message.setProjectId(projectId);
        message.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Long messageId = chatRoomService.saveMessage(message);
        notificationService.create(NotificationType.CHATTING,notificationService.makeChattingContent(message.getUserId(),messageId),projectId,null,null);
        return message;
    }

    @Operation(summary = "채팅방 전체 내역 조회", description = "채팅방의 전체 메세지 내역을 조회합니다.")
    @GetMapping("/room/{projectId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@PathVariable Long projectId) { // 프론트 연동 OK
        List<ChatMessageDto> chatHistory = chatRoomService.getChatHistory(projectId);
        return ResponseEntity.ok(chatHistory);
    }

    @Operation(summary = "채팅방 전체 내역 페이지네이션 조회", description = "채팅방의 전체 메세지 내역을 페이지네이션 해서 조회합니다.")
    @GetMapping("/room/{projectId}/messages/paged")
    public ResponseEntity<Page<ChatMessageDto>> getChatHistoryPaginated( // 프론트 연동 OK
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<ChatMessageDto> chatHistory = chatRoomService.getChatHistoryPaginated(projectId, page, size);
        return ResponseEntity.ok(chatHistory);
    }
}
