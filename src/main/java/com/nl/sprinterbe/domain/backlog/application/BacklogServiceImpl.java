package com.nl.sprinterbe.domain.backlog.application;

import com.nl.sprinterbe.domain.backlog.dao.BacklogRepository;
import com.nl.sprinterbe.domain.backlog.dto.*;
import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.dailyScrum.dto.BacklogResponse;
import com.nl.sprinterbe.domain.issue.dao.IssueRepository;
import com.nl.sprinterbe.domain.issue.entity.Issue;
import com.nl.sprinterbe.domain.sprint.dao.SprintRepository;
import com.nl.sprinterbe.domain.sprint.entity.Sprint;
import com.nl.sprinterbe.domain.task.dao.TaskRepository;
import com.nl.sprinterbe.domain.task.entity.Task;
import com.nl.sprinterbe.domain.user.dao.UserRepository;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userBacklog.dao.UserBacklogRepository;
import com.nl.sprinterbe.domain.userBacklog.entity.UserBacklog;
import com.nl.sprinterbe.global.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BacklogServiceImpl implements BacklogService {

    private final TaskRepository taskRepository;
    private final BacklogRepository backlogRepository;
    private final UserBacklogRepository userBacklogRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Override
    @Transactional(readOnly = true)
    public Slice<BacklogInfoResponse> findBacklogListByProjectId(Long projectId, Long userId,Pageable pageable ) {
        return backlogRepository.findByProjectIdDesc(projectId,userId, pageable).map(BacklogInfoResponse::of);
    }


    @Override
    @Transactional(readOnly = true)
    public BacklogDetailResponse findBacklogDetailById(Long backlogId) {
        Backlog backlog = backlogRepository.findByBacklogId(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 백로그가 없습니다."));
        return BacklogDetailResponse.of(backlog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogTaskResponse> findTaskByBacklogId(Long backlogId) {
        List<Task> tasks = backlogRepository.findTasksByBacklogId(backlogId);
        if(tasks.isEmpty()) {
            throw new NoDataFoundException("해당 Id로 조회된 Task가 없습니다.");
        }
        return tasks.stream().map(BacklogTaskResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogUserResponse> findUserByBacklogId(Long backlogId) {
        List<User> users = userBacklogRepository.findUsersByBacklogId(backlogId);
        if(users.isEmpty()) {
            throw new NoDataFoundException("해당 Id로 조회된 유저가 없습니다.");
        }
        return users.stream().map(BacklogUserResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogIssueResponse> findIssueByBacklogId(Long backlogId) {
        List<Issue> issues = backlogRepository.findIssuesByBacklogId(backlogId);
        if(issues.isEmpty()) {
            throw new NoDataFoundException("해당 Id로 조회된 Issue가 없습니다.");
        }
        return issues.stream().map(BacklogIssueResponse::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogUserResponse> findBacklogExceptUsers(Long projectId, Long backlogId) {
        List<User> users = backlogRepository.findUsersNotInBacklog(projectId, backlogId);
        return users.stream().map(BacklogUserResponse::of).collect(Collectors.toList());
    }

    // Sprint 찾기
    //title 만 가지고 Backlog 만들기 + Sprint 주입
    // 각 users의 userId , backlogId 만든 후 backlogId로 UserBacklog에 연관관계 주입
    // backlogId와 task의 content로 연관관계 주입
    // content와 checked=false로 issue 연관관계 주입
    @Override
    public BacklogPostResponse createBacklog(BacklogPostRequest request,Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 Sprint가 없습니다."));
        Backlog newBacklog = Backlog.builder().title(request.getTitle()).sprint(sprint).isFinished(false).weight(request.getWeight()).build();
        backlogRepository.save(newBacklog);
        List<User> savedUsers = getUsers(request, newBacklog);
        List<Task> savedTasks = getTasks(request, newBacklog);
        Issue newIssue = Issue.builder().backlog(newBacklog).checked(false).content(request.getIssue().getContent()).build();
        issueRepository.save(newIssue);

        return BacklogPostResponse.of(newBacklog,request.getWeight(),savedUsers,savedTasks,newIssue);
    }

    @Override
    public BacklogTitleResponse updateBacklogTitle(BacklogTitleRequest request, Long backlogId) {
        Backlog backlog = backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 Backlog 가 없습니다."));
        backlog.setTitle(request.getTitle());

        return BacklogTitleResponse.of(backlog);
    }

    public List<BacklogUserResponse> updateBacklogUsers(Long backlogId, BacklogUserUpdateRequest request) {
        // Backlog 엔티티 조회 (없으면 예외 발생)
        Backlog backlog = backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NoDataFoundException("해당 Backlog가 없습니다."));

        // 추가할 유저 처리
        if (request.getAddUserIds() != null) {
            request.getAddUserIds().forEach(userId -> {
                // 해당 유저가 있는지 확인
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 User가 없습니다."));
                // 이미 연결되어 있는지 체크 (중복 연결 방지)
                if (!userBacklogRepository.existsByUserAndBacklog(user, backlog)) {
                    userBacklogRepository.save(new UserBacklog(user, backlog));
                }
            });
        }

        // 삭제할 유저 처리
        if (request.getRemoveUserIds() != null) {
            request.getRemoveUserIds().forEach(userId -> {
                // 기존 연결이 있으면 삭제
                userBacklogRepository.deleteByUserIdAndBacklogId(userId, backlogId);
            });
        }


        return userBacklogRepository.findUsersByBacklogId(backlogId).stream()
                .map(BacklogUserResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public BacklogUserResponse addBacklogUser(Long backlogId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 User가 없습니다."));
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 Backlog가 없습니다."));
        userBacklogRepository.save(new UserBacklog(user, backlog));
        return BacklogUserResponse.of(user);
    }

    @Override
    public List<BacklogTaskResponse> updateBacklogTasks(Long backlogId, BacklogTaskUpdateRequest request) {
        // Backlog 엔티티 조회 (없으면 예외 발생)
        Backlog backlog = backlogRepository.findById(backlogId)
                .orElseThrow(() -> new NoDataFoundException("해당 Backlog가 없습니다."));

        // 추가할 Task 처리
        if (request.getAddTasks() != null) {
            request.getAddTasks().forEach(taskDto -> {
                Task newTask = Task.builder()
                        .backlog(backlog)
                        .content(taskDto.getContent())
                        .build();
                taskRepository.save(newTask);
            });
        }

        // 삭제할 Task 처리
        if (request.getRemoveTaskIds() != null) {
            request.getRemoveTaskIds().forEach(taskId -> {
                // 각 Task가 실제 Backlog와 연관되어 있는지 확인 후 삭제할 수 있음
                // 단순 삭제의 경우 아래와 같이 deleteById로 삭제
                // (추가 검증이 필요하면 taskRepository.findById(taskId) 등으로 확인)
                taskRepository.deleteById(taskId);
            });
        }
        return taskRepository.findByBacklogId(backlogId)
                .stream()
                .map(BacklogTaskResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long backlogId, Long userId) {
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id의 Backlog가 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("해당 Id의 User가 없습니다."));
        UserBacklog find = userBacklogRepository.findByUserAndBacklog(user, backlog).orElseThrow(() -> new NoDataFoundException("해당 userId와 backlogId의 User가 없습니다."));

        userBacklogRepository.delete(find);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public BacklogTaskResponse addTask(Long backlogId, BacklogTaskRequest request) {
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id의 Backlog가 없습니다."));
        Task task = Task.builder().backlog(backlog).content(request.getContent()).build();
        taskRepository.save(task);
        return BacklogTaskResponse.of(task);
    }

    @Override
    public BacklogIssueResponse addIssue(Long backlogId, BacklogIssueRequest request) {
        Backlog backlog = backlogRepository.findById(backlogId).orElseThrow(() -> new NoDataFoundException("해당 Id의 Backlog가 없습니다."));
        Issue issue = Issue.builder().backlog(backlog).checked(false).content(request.getContent()).build();
        issueRepository.save(issue);
        return BacklogIssueResponse.of(issue);
    }

    @Override
    public BacklogIssueResponse updateIssue(Long issueId, BacklogIssueRequest request) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NoDataFoundException("해당 Id의 Issue가 없습니다."));
        issue.setContent(request.getContent());
        return BacklogIssueResponse.of(issue);
    }

    @Override
    public void deleteIssue(Long issueId) {
        issueRepository.deleteById(issueId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BacklogResponse> getBacklogsExcludeDailyScrum(Long sprintId, Long dailyScrumId) {
        List<Backlog> backlogs = backlogRepository.findExcludingDailyScrum(sprintId, dailyScrumId);
        return backlogs.stream()
                .map(BacklogResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductBacklogResponse> getBacklogsByProject(Long projectId) {
        List<Backlog> backlogs = backlogRepository.findBacklogsByProjectId(projectId);
        return backlogs.stream().map(ProductBacklogResponse::of).collect(Collectors.toList());
    }

    @NotNull
    private List<Task> getTasks(BacklogPostRequest request, Backlog newBacklog) {
        List<Task> savedTasks = request.getTasks().stream()
                .map(taskDto -> taskRepository.save(
                        Task.builder()
                                .backlog(newBacklog)
                                .content(taskDto.getContent())
                                .build())
                )
                .collect(Collectors.toList());
        return savedTasks;
    }

    @NotNull
    private List<User> getUsers(BacklogPostRequest request, Backlog newBacklog) {
        List<User> savedUsers = request.getUsers().stream()
                .map(userDto -> {
                    User foundUser = userRepository.findById(userDto.getUserId())
                            .orElseThrow(() -> new NoDataFoundException("해당 Id로 조회된 User가 없습니다."));
                    userBacklogRepository.save(new UserBacklog(foundUser, newBacklog));
                    return foundUser;
                })
                .collect(Collectors.toList());
        return savedUsers;
    }



}
