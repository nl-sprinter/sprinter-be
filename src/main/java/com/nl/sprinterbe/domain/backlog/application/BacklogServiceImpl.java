package com.nl.sprinterbe.domain.backlog.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.dto.BacklogDetailResponse;
import com.nl.sprinterbe.domain.backlog.dto.BacklogInfoResponse;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userBacklog.dao.UserBacklogRepository;
import com.nl.sprinterbe.global.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BacklogServiceImpl implements BacklogService {

    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;
    private final BacklogRepository backlogRepository;
    private final UserBacklogRepository userBacklogRepository;

    @Override
    public Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId,Pageable pageable ) {
        return backlogRepository.findByProjectIdDesc(projectId,userId, pageable).map(BacklogInfoResponse::of);
    }

    @Override
    public Slice<BacklogInfoResponse> findBacklogListBySprintId(Long sprintId, Long userId, Pageable pageable) {
        return backlogRepository.findBySprintIdDesc(sprintId,pageable).map(BacklogInfoResponse::of);
    }

    @Override
    public BacklogDetailResponse findBacklogDetailById(Long backlogId) {
        Backlog backlog = backlogRepository.findByBacklogId(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 데이터가 없습니다."));
        List<User> users = userBacklogRepository.findUsersByBacklogId(backlogId);

    }
}
