package com.nl.sprinterbe.domain.backlogcomment.service;

import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentFromResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentUpdateContent;

import java.util.List;

public interface BacklogCommentService {

    BacklogCommentFromResponse createBacklogComment(Long backlogId, Long userId, BacklogCommentRequest request);

    void deleteBacklogComment(Long userId, Long commentId);

    List<BacklogCommentResponse> getBacklogComments(Long backlogId);

}
