package com.example.rewardservice.point;

import com.example.rewardservice.point.domain.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointRepository extends JpaRepository<PointLog, UUID> {

}
