package com.nl.sprinterbe.domain.backlogComment.api;

import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogComment.service.BacklogCommentService;
import com.nl.sprinterbe.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/backlogComment")
@RequiredArgsConstructor
public class BacklogCommentController {

    private final BacklogCommentService backlogCommentService;
    private final JwtUtil jwtUtil;

    @PostMapping("create/{backlogId}")
    public ResponseEntity<BacklogCommentResponse> createComment(
            @PathVariable Long backlogId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.createComment(backlogId,jwtUtil.removeBearer(token),request));
    }

    @PatchMapping("update/{commentId}")
    public ResponseEntity<BacklogCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Validated BacklogCommentRequest request,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.updateComment(jwtUtil.removeBearer(token),commentId,request));
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<BacklogCommentResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.deleteComment(jwtUtil.removeBearer(token),commentId));
    }

    //본인 댓글만 반환
    @GetMapping("user")
    public ResponseEntity<List<BacklogCommentResponse>> getUserComment(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(backlogCommentService.getUserComment(jwtUtil.removeBearer(token)));
    }

    //해당 Backlog에 있는 모든 댓글 반환(형태는 실제 댓글, 대댓글 형식으로 리턴)
    @GetMapping("{backlogId}")
    public ResponseEntity<List<BacklogCommentResponse>> getComments(
            @PathVariable Long backlogId) {
        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
    }

}
