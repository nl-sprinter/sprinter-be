package com.nl.sprinterbe.domain.notification.api;

import com.nl.sprinterbe.domain.notification.application.NotificationService;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;
    // 알림을 읽었다는게 모달을 열때? 아니면 알림을 클릭할때? (알림 각각을 눌렀을때)
    // User가 알림을 읽어서 알림 카운트를 0으로 만들때 URL이 patch("/notification/count") 인데 /notification/users/{userId}/count 처럼 userId가 있어야 되는거 아닌가?
    // 알림 전체 삭제에서도 .delete("/notifications") 에서 /notifications/users/{userId} 로 userId 필요한거 아닌가? >> 토큰에서 파싱해서 쓰는 UserId로 바로

    // 알림을 읽었다는건 isRead = true; 로 >> 읽었다는거 자체가 필요가 없음. 그냥 일단 다 뿌려주고 알림 갯수 배너는 이 뿌려준것을 기반으로
    // lms처럼 이전 알림을 읽었음에도 불구하고 보내줌 >> 페이징 처리?

    /**
     * 특정 User의 알림 Dto 반환
     *  userId를 기반으로 Notification 정보
     *
     *  create 트리거:
     *  댓글 / 대댓글
     *  이슈등록
     *  새 팀원 프로젝트 합류
     *  데일리스크럼 팀원 추가
     *  스케줄 알림
     *  채팅 (보류 중)
     */

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(notificationService.findNotificationsByUserId(jwtUtil.getUserIdByToken(token)));
    }

    /**
     * 알림 카운트 조회
     * userId를 기반으로 DB에 남아있는 알림 Count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String,Long>> getCountNotifications(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(notificationService.countNotificationByUserId(jwtUtil.getUserIdByToken(token)));
    }

    /**
     * 알림 delete는 단건 : notificationId
     * 전체 : userId
     */
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotificationByNotificationId(notificationId);
        return ResponseEntity.ok().build();
    }

    // 알림 전체 삭제
    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteAllNotifications(@RequestHeader("Authorization") String token) {
        notificationService.deleteAllNotificationsByUserId(jwtUtil.getUserIdByToken(token));
        return ResponseEntity.ok().build();
    }

}
