package com.example.rewardservice.point;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.domain.EarnedPoint;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EarnedPointRepository extends JpaRepository<EarnedPoint, UUID> {
    List<EarnedPoint> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
    List<EarnedPoint> findByUserAndEventAndCreatedAtBetween(User user, Event event, LocalDateTime start, LocalDateTime end);
}
