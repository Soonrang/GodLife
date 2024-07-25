package com.example.rewardservice.event.domain.repository;

import com.example.rewardservice.event.domain.Events.AttendanceEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceEventRepository extends JpaRepository<AttendanceEvent, UUID> {
}
