package com.nl.sprinterbe.domain.backlogcomment.service;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.backlogcomment.dao.BacklogCommentRepository;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentRequest;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentFromResponse;
import com.nl.sprinterbe.domain.backlogcomment.dto.BacklogCommentResponse;
import com.nl.sprinterbe.domain.backlogcomment.entity.BacklogComment;
import com.nl.sprinterbe.domain.like.dao.LikeRepository;
import com.nl.sprinterbe.domain.like.entity.Like;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.global.exception.backlog.BacklogNotFoundException;
import com.nl.sprinterbe.global.exception.backlogcomment.BacklogCommentNotFoundException;
import com.nl.sprinterbe.global.exception.backlogcomment.ForbiddenCommentAccessException;
import com.nl.sprinterbe.global.exception.user.UserNotFoundException;
import com.nl.sprinterbe.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BacklogCommentServiceImpl implements BacklogCommentService {

    private final BacklogCommentRepository backlogCommentRepository;
    private final UserRepository userRepository;
    private final BacklogRepository backlogRepository;
    private final LikeRepository likeRepository;
    private final SecurityUtil securityUtil;

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

        return getCommentResponses(comments);
    }

    @Override
    public List<BacklogCommentResponse> onLikeToBacklogComment(Long backlogId, Long backlogCommentId) {
        Long userId = Long.parseLong(securityUtil.getCurrentUserId().get());

        if (!likeRepository.existsByUserUserIdAndBacklogCommentBacklogCommentId(userId, backlogCommentId)) {
            BacklogComment backlogComment = backlogCommentRepository.findById(backlogCommentId)
                    .orElseThrow(BacklogCommentNotFoundException::new);
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

            Like like = Like.builder()
                    .backlogComment(backlogComment)
                    .user(user)
                    .build();

            likeRepository.save(like);
        }

        return getBacklogComments(backlogId);
    }

    @Override
    public List<BacklogCommentResponse> offLikeToBacklogComment(Long backlogId, Long backlogCommentId) {
        Long userId = Long.parseLong(securityUtil.getCurrentUserId().get());
        likeRepository.deleteByBacklogCommentBacklogCommentIdAndUserUserId(backlogCommentId, userId);

        return getBacklogComments(backlogId);
    }

    /**
     * 댓글 리스트에 달린 좋아요를 가져와서 댓글 정보와 좋아요 수를 함께 반환하는 메서드
     * @param comments 해당 백로그에 달린 댓글 리스트
     */
    @NotNull
    private List<BacklogCommentResponse> getCommentResponses(List<BacklogComment> comments) {
        List<Long> commentIds = comments.stream()
                .map(BacklogComment::getBacklogCommentId)
                .toList();

        //댓글 좋아요 수를 group by로 가져와 쿼리 한번에 모든 정보를 가져 온다. id를 key로 map에 저장을 한 후 dto에 실어 리턴
        Map<Long,Long> likeCountMap = commentIds.isEmpty() ?
                Collections.emptyMap() :
                likeRepository.countLikesByBacklogCommentIds(commentIds).stream()
                        .collect(Collectors.toMap(
                                likeCount -> (Long) likeCount[0],
                                likeCount -> (Long) likeCount[1]
                        ));

        return comments.stream()
                .map(comment -> BacklogCommentResponse.of(
                        comment,
                        likeCountMap.getOrDefault(comment.getBacklogCommentId(), 0L)))
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
