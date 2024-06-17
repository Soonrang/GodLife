package com.example.rewardservice.repository;

import com.example.rewardservice.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointRepository extends JpaRepository<Point, UUID> {

    Point findByUserId(String userId);

}
