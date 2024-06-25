package com.example.rewardservice.repository;

import com.example.rewardservice.domain.User.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, UUID> {
    boolean existsByUserIdAndEventIdAndAttendanceDateBetween(String userId, UUID eventID);
    long countByUserIdAndEventId(String userId, UUID eventId);
}
