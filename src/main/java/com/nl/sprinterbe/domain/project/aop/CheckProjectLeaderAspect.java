package com.nl.sprinterbe.domain.project.aop;

import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.userproject.dao.UserProjectRepository;
import com.nl.sprinterbe.global.exception.userproject.UserIsNotProjectLeaderException;
import com.nl.sprinterbe.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class CheckProjectLeaderAspect {

    private final SecurityUtil securityUtil;
    private final UserProjectRepository userProjectRepository;

    @Before(value = "@annotation(CheckProjectLeaderBefore)")
    public void checkProjectLeader(JoinPoint joinPoint) {
        Long userId = Long.parseLong(securityUtil.getCurrentUserId().orElseThrow(UserIsNotProjectLeaderException::new));
        Long projectId = (Long)joinPoint.getArgs()[0];
        //유저가 프로젝트 리더인지 확인
    }
}
