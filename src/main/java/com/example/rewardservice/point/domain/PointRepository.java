package com.example.rewardservice.point.domain;

import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PointRepository extends JpaRepository<Point, UUID> {
    List<Point> findByUser(User user);

    List<Point> findByUserAndType(User user, String type);

    List<Point> findByUserAndTypeOrderByCreatedAtDesc(User user, String type);
}
