package com.nl.sprinterbe.domain.admin.api;

import com.nl.sprinterbe.domain.admin.application.AdminService;
import com.nl.sprinterbe.domain.admin.dto.AlarmRequest;
import com.nl.sprinterbe.domain.admin.dto.UserRequest;
import com.nl.sprinterbe.domain.notification.application.NotificationService;
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

    @GetMapping("/users")
    public ResponseEntity<Page<UserRequest>> getAllUsers(
            @PageableDefault(size = 20, page = 0) Pageable pageable,
            @RequestParam(required = false) String searchTerm) {
        System.out.println("searchTerm = " + searchTerm);
        return ResponseEntity.ok(adminService.searchUsers(searchTerm, pageable));
    }

    /**
     *
     * @param keyword
     * @param pageable
     * @return
     * 반환 예시 Dto
     * {
     *     "content": [
     *         {
     *             "userId": 3,
     *             "nickName": null,
     *             "password": "******",
     *             "email": "test3",
     *             "roleUser": "ROLE_USER"
     *         }
     *     ],
     *     "pageable": {
     *         "pageNumber": 0,
     *         "pageSize": 20,
     *         "sort": {
     *             "empty": true,
     *             "sorted": false,
     *             "unsorted": true
     *         },
     *         "offset": 0,
     *         "paged": true,
     *         "unpaged": false
     *     },
     *     "last": true,
     *     "totalElements": 1,
     *     "totalPages": 1,
     *     "size": 20,
     *     "number": 0,
     *     "sort": {
     *         "empty": true,
     *         "sorted": false,
     *         "unsorted": true
     *     },
     *     "first": true,
     *     "numberOfElements": 1,
     *     "empty": false
     * }
     */

    /**
     * @Dto
     * {
     *   "userId": [3, 4, 5]
     * }
     */
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUsers(@RequestBody Map<String,List<Long>> request) {
        List<Long> userIds = request.get("userIds");
        adminService.deleteUser(userIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/message/selected")
    public ResponseEntity<Void> sendAlarm(@RequestBody AlarmRequest request) {
        notificationService.sendAlarmToUsers(request);
        return ResponseEntity.ok().build();
    }



}
