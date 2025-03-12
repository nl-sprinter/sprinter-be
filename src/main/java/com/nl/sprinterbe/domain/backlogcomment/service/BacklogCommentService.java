package com.nl.sprinterbe.domain.backlogcomment.service;

import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentFromResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentUpdateContent;

import java.util.List;

public interface BacklogCommentService {

    BacklogCommentFromResponse createComment(Long backlogId, Long userId, BacklogCommentRequest request);

    BacklogCommentFromResponse updateComment(Long userId, Long commentId, BacklogCommentUpdateContent request);

    BacklogCommentFromResponse deleteComment(Long userId, Long commentId);

    List<BacklogCommentFromResponse> getUserComment(Long userId);

    List<BacklogCommentResponse> getComments(Long backlogId);

    List<BacklogCommentFromResponse> getStructComments(Long backlogId);
}
