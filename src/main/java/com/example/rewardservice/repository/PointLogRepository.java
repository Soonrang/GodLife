package com.example.rewardservice.repository;

import com.example.rewardservice.domain.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PointLogRepository extends JpaRepository<PointLog, UUID> {
    List<PointLog> findByUserId(UUID userId);
}
