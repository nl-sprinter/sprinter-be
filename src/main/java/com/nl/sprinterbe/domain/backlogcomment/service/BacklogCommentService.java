package com.nl.sprinterbe.domain.backlogcomment.service;

import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentUpdateContent;

import java.util.List;

public interface BacklogCommentService {

    BacklogCommentResponse createComment(Long backlogId, Long userId, BacklogCommentRequest request);

    BacklogCommentResponse updateComment(Long userId, Long commentId, BacklogCommentUpdateContent request);

    BacklogCommentResponse deleteComment(Long userId, Long commentId);

    List<BacklogCommentResponse> getUserComment(Long userId);

    List<BacklogCommentResponse> getComments(Long backlogId);
}
