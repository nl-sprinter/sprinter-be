package com.nl.sprinterbe.userschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {

}
