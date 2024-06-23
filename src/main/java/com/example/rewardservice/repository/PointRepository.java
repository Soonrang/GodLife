package com.example.rewardservice.repository;

import com.example.rewardservice.domain.Point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PointRepository extends JpaRepository<Point, UUID> {

    Point findByUserId(UUID userId);
    Optional<Point> findByUserId(String userId);

}
