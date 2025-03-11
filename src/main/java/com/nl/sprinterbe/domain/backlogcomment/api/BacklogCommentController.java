//package com.nl.sprinterbe.domain.backlogComment.api;
//
//import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
//import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentResponse;
//import com.nl.sprinterbe.domain.backlogComment.service.BacklogCommentService;
//import com.nl.sprinterbe.global.security.JwtUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/backlogComment")
//@RequiredArgsConstructor
//@Tag(name = "BacklogComment API", description = "백로그 댓글 관련 API 입니다.")
//public class BacklogCommentController {
//
//    private final BacklogCommentService backlogCommentService;
//    private final JwtUtil jwtUtil;
//
//    @Operation(summary = "댓글 생성", description = "백로그에 댓글을 생성합니다.")
//    @PostMapping("create/{backlogId}")
//    public ResponseEntity<BacklogCommentResponse> createComment(
//            @PathVariable Long backlogId,
//            @RequestBody @Validated BacklogCommentRequest request,
//            @RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(backlogCommentService.createComment(backlogId,jwtUtil.removeBearer(token),request));
//    }
//
//    @Operation(summary = "댓글 수정", description = "백로그에 댓글을 수정합니다.")
//    @PatchMapping("update/{commentId}")
//    public ResponseEntity<BacklogCommentResponse> updateComment(
//            @PathVariable Long commentId,
//            @RequestBody @Validated BacklogCommentRequest request,
//            @RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(backlogCommentService.updateComment(jwtUtil.removeBearer(token),commentId,request));
//    }
//
//    @Operation(summary = "댓글 삭제", description = "백로그에 댓글을 삭제합니다.")
//    @DeleteMapping("delete/{commentId}")
//    public ResponseEntity<BacklogCommentResponse> deleteComment(
//            @PathVariable Long commentId,
//            @RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(backlogCommentService.deleteComment(jwtUtil.removeBearer(token),commentId));
//    }
//
//    @Operation(summary = "내 댓글 조회", description = "내가 작성한 댓글을 조회합니다.")
//    @GetMapping("user")
//    public ResponseEntity<List<BacklogCommentResponse>> getUserComment(
//            @RequestHeader("Authorization") String token) {
//        return ResponseEntity.ok(backlogCommentService.getUserComment(jwtUtil.removeBearer(token)));
//    }
//
//    @Operation(summary = "댓글 조회", description = "백로그에 달린 댓글을 조회합니다.")
//    @GetMapping("{backlogId}")
//    public ResponseEntity<List<BacklogCommentResponse>> getComments(
//            @PathVariable Long backlogId) {
//        return ResponseEntity.ok(backlogCommentService.getComments(backlogId));
//    }
//
//}
