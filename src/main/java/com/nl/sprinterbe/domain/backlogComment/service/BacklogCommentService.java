package com.nl.sprinterbe.domain.backlogComment.service;

import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentResponse;

import java.util.List;

public interface BacklogCommentService {

    BacklogCommentResponse createComment(Long backlogId, Long userId, BacklogCommentRequest request);

    BacklogCommentResponse updateComment(Long userId, Long commentId, BacklogCommentRequest request);

    BacklogCommentResponse deleteComment(Long userId, Long commentId);

    List<BacklogCommentResponse> getUserComment(Long userId);

    List<BacklogCommentResponse> getComments(Long backlogId);
}
