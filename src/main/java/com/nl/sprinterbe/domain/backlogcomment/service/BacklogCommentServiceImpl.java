package com.nl.sprinterbe.domain.backlogcomment.service;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogcomment.dao.BacklogCommentRepository;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentFromResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.backlogcomment.BacklogCommentNotFoundException;
import com.nl.sprinterbe.global.exception.backlogcomment.ForbiddenCommentAccessException;
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
    public BacklogCommentFromResponse createBacklogComment(Long backlogId, Long userId, BacklogCommentRequest request) {
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(BacklogNotFoundException::new);
        Optional<User> user = userRepository.findById(userId);
        BacklogComment newComment = BacklogComment.of(request, backlog, user);

        if(request.getParentCommentId() != null) {
            newComment.setParentCommentId(request.getParentCommentId());
        }

        BacklogComment savedComment = backlogCommentRepository.save(newComment);
        return BacklogCommentFromResponse.of(savedComment);
    }


    @Override
    public void deleteBacklogComment(Long userId, Long commentId) {
        BacklogComment comment = backlogCommentRepository.findById(commentId).orElseThrow(BacklogCommentNotFoundException::new);
        if(!comment.getUser().getUserId().equals(userId)) {throw new ForbiddenCommentAccessException();}

        backlogCommentRepository.delete(comment);
    }


    @Override
    public List<BacklogCommentResponse> getBacklogComments(Long backlogId) {
        List<BacklogComment> comments = backlogCommentRepository.findCommentsByBacklogId(backlogId);
        return comments.stream()
                .map(BacklogCommentResponse::of)
                .toList();
    }

    /**
     * 댓글 리스트를 부모와 자식 관계로 만들어줘서 반환하는 메서드
     * @param comments 해당 백로그에 달린 댓글 리스트
     */
    @NotNull
    private static List<BacklogCommentFromResponse> getBacklogCommentResponses(List<BacklogComment> comments) {
        List<BacklogCommentFromResponse> commentResponses = new ArrayList<>();
        Map<Long, BacklogCommentFromResponse> map = new HashMap<>();
        for(BacklogComment comment : comments) {
            BacklogCommentFromResponse commentResponse = BacklogCommentFromResponse.of(comment);

            map.put(commentResponse.getBacklogCommentId(), commentResponse);
            if (comment.getParentCommentId() != null) {
                map.get(comment.getParentCommentId())
                        .getChildComments()
                        .add(commentResponse);
            } else {
                commentResponses.add(commentResponse);
            }
        }
        return commentResponses;
    }


}
