package com.nl.sprinterbe.domain.admin.api;

import com.nl.sprinterbe.domain.admin.application.AdminService;
import com.nl.sprinterbe.domain.admin.dto.AlarmRequest;
import com.nl.sprinterbe.domain.admin.dto.UserRequest;
import com.nl.sprinterbe.domain.notification.application.NotificationService;
import com.nl.sprinterbe.domain.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<UserRequest>> getAllUsers(
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserRequest>> searchUsers(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        return ResponseEntity.ok(adminService.searchUsers(keyword, pageable));
    }

    /**
     * @Dto
     * {
     *   "userIds": [3, 4, 5]
     * }
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody Map<String,List<Long>> request) {
        List<Long> userIds = request.get("userId");
        adminService.deleteUser(userIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/alarm")
    public ResponseEntity<Void> sendAlarm(@RequestBody AlarmRequest request) {
        notificationService.sendAlarmToUsers(request);
        return ResponseEntity.ok().build();
    }



}
