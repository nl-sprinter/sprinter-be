package com.nl.sprinterbe.domain.backlogComment.service;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogComment.dao.BacklogCommentRepository;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogComment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogComment.entity.BacklogComment;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.backlogComment.BacklogCommentNotFoundException;
import com.nl.sprinterbe.global.exception.backlogComment.ForbiddenCommentAccessException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BacklogCommentServiceImpl implements BacklogCommentService {

    private final BacklogCommentRepository backlogCommentRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;

    @Override
    public BacklogCommentResponse createComment(Long backlogId, Long userId, BacklogCommentRequest request) {
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(() -> new BacklogNotFoundException());
        Optional<User> user = userRepository.findById(userId);
        BacklogComment parent = null;
        BacklogComment newComment = BacklogComment.of(request, backlog, user);

        if(request.getParentCommentId() != null) {
            parent = backlogCommentRepository.findById(request.getParentCommentId()).orElseThrow(() -> new BacklogCommentNotFoundException());
            newComment.setParent(parent);
        }

        BacklogComment savedComment = backlogCommentRepository.save(newComment);
        return BacklogCommentResponse.of(savedComment);
    }



    @Override
    public BacklogCommentResponse updateComment(Long userId, Long commentId, BacklogCommentRequest request) {
        BacklogComment comment = backlogCommentRepository.findById(commentId).orElseThrow(() -> new BacklogCommentNotFoundException());
        if(comment.getUser().getUserId() != userId) {throw new ForbiddenCommentAccessException();}

        comment.setContent(request.getContent());
        BacklogComment savedComment = backlogCommentRepository.save(comment);
        return BacklogCommentResponse.of(savedComment);

    }

    @Override
    public BacklogCommentResponse deleteComment(Long userId, Long commentId) {
        BacklogComment comment = backlogCommentRepository.findById(commentId).orElseThrow(() -> new BacklogCommentNotFoundException());
        if(comment.getUser().getUserId() != userId) {throw new ForbiddenCommentAccessException();}

        backlogCommentRepository.delete(comment);
        return BacklogCommentResponse.of(comment);
    }

    @Override
    public List<BacklogCommentResponse> getUserComment(Long userId) {
        List<BacklogComment> comments = backlogCommentRepository.findByUserUserId(userId);
        List<BacklogCommentResponse> commentResponses = comments.stream()
                .map(BacklogCommentResponse::of)
                .toList();
        return commentResponses;
    }


    @Override
    public List<BacklogCommentResponse> getComments(Long backlogId) {
        List<BacklogComment> comments = backlogCommentRepository.findCommentsByBacklogId(backlogId);
        List<BacklogCommentResponse> commentResponses = getBacklogCommentResponses(comments);
        return commentResponses;
    }

    @NotNull
    private static List<BacklogCommentResponse> getBacklogCommentResponses(List<BacklogComment> comments) {
        List<BacklogCommentResponse> commentResponses = new ArrayList<>();
        Map<Long, BacklogCommentResponse> map = new HashMap<>();
        for(BacklogComment comment : comments) {
            BacklogCommentResponse commentResponse = BacklogCommentResponse.of(comment);

            map.put(commentResponse.getBacklogCommentId(), commentResponse);
            if (comment.getParentComment() != null) {
                map.get(comment.getParentComment().getBacklogCommentId()).getChildComments().add(commentResponse);
            } else {
                commentResponses.add(commentResponse);
            }
        }
        return commentResponses;
    }


}
