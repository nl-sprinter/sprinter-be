package com.nl.sprinterbe.domain.notification.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.notification.dao.NotificationRepository;
import com.nl.sprinterbe.domain.notification.dto.NotificationDto;
import com.nl.sprinterbe.domain.notification.entity.Notification;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import com.nl.sprinterbe.domain.notification.entity.UserNotification;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.domain.schedule.dao.ScheduleRepository;
import com.nl.sprinterbe.domain.schedule.entity.Schedule;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import com.nl.sprinterbe.global.exception.schedule.ScheduleNotFoundException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final ScheduleRepository scheduleRepository;

    public void create(NotificationType notificationType, String content, Long projectId, String url) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<User> users = projectRepository.findAllUsersByProjectId(projectId);

        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .content(content)
                .project(project)
                .createdAt(LocalDateTime.now())
                .navigable(url != null)
                .url(url)
                .build();

        notification.setUserNotification(
                users.stream()
                        .map(user -> UserNotification.builder().users(user).notification(notification).build())
                        .toList() // List<UserNotification>으로 변환
        );


        notificationRepository.save(notification);
    }

    public List<NotificationDto> findNotificationsByUserId(Long userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    public Map<String,Long> countNotificationByUserId(Long userId){
        Long count = notificationRepository.countNotificationByUserId(userId);
        return Map.of("count", count);
    }

    public void deleteNotificationByNotificationId(Long notificationId) {
        notificationRepository.deleteByNotificationId(notificationId);
    }

    public void deleteAllNotificationsByUserId(Long userId) {
        notificationRepository.deleteNotificationByUserId(userId);
    }

    public String makeCommentContent(Long senderId,Long backlogId) {
        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(BacklogNotFoundException::new);
        String senderName = sender.getNickname();
        return senderName + "님이\""  +backlog.getTitle() + "\" 백로그에 새로운 댓글을 남겼습니다.";
    }

    public String makeCommentUrl(Long projectId , Long sprintId , Long backlogId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/backlogs/" + backlogId;
    }

    public String makeIssueContent(Long senderId, Long backlogId) {
        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(BacklogNotFoundException::new);
        String senderName = sender.getNickname();
        return senderName + "님이\"" + backlog.getTitle() + "\" 백로그에 새로운 이슈를 등록했습니다.";
    }

    public String makeIssueUrl(Long projectId , Long sprintId , Long backlogId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/backlogs/" + backlogId;
    }

    public String makeDailyScrumContent(Long senderId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime + "에 데일리 스크럼이 예정되어 있습니다.";
    }

    public String makeDailyScrumUrl(Long projectId , Long sprintId , Long dailyScrumId){
        return "/projects/" + projectId + "/sprints/" + sprintId + "/dailyScrums/" + dailyScrumId;
    }

    public String makeTeammateContent(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getNickname() + "님이 프로젝트에 참여했습니다.";
    }

    // 스케줄 알림 시간 전에 Request 없이 해야됨.
    public String makeScheduleContent(Long scheduleId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        String formattedDateTime = now.format(formatter);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        return formattedDateTime + "에 \""+ schedule.getTitle() +"\"가 예정되어 있습니다.";
    }

    public String makeScheduleUrl(Long projectId ,Long scheduleId){
        return "/projects/" + projectId + "/calendar/schedule/" + scheduleId;
    }

    // 채팅 시 알람
//    public String makeChattingContent(Long senderId, Long chattingId) {
//        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
//        String senderName = sender.getNickname();
//
//        return senderName + "님이 새로운 메시지를 보냈습니다: \"" + 채팅 메시지
//    }

//    public String makeChattingUrl(Long projectId , Long sprintId , Long backlogId){
//        return "/projects/" +projectId
//    }

}
