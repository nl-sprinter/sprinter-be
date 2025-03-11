package com.nl.sprinterbe.domain.userbacklog.dao;

import com.nl.sprinterbe.domain.backlog.entity.Backlog;
import com.nl.sprinterbe.domain.user.entity.User;
import com.nl.sprinterbe.domain.userbacklog.entity.UserBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBacklogRepository extends JpaRepository<UserBacklog, Long> {
    @Query("SELECT u.user FROM UserBacklog u WHERE u.backlog.backlogId = :backlogId")
    List<User> findUsersByBacklogId(@Param("backlogId") Long backlogId);

    boolean existsByUserAndBacklog(User user, Backlog backlog);

    @Modifying
    @Query("DELETE FROM UserBacklog ub WHERE ub.user.userId = :userId AND ub.backlog.backlogId = :backlogId")
    void deleteByUserIdAndBacklogId(@Param("userId") Long userId, @Param("backlogId") Long backlogId);

    Optional<UserBacklog> findByUserAndBacklog(User user, Backlog backlog);
}
