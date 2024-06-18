package com.example.rewardservice.service;

import com.example.rewardservice.domain.Point;
import com.example.rewardservice.domain.PointLog;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    @Transactional
    public Point createPoint(String userId, long initialPoints) {
        Point point = new Point();
        point.setUserId(userId);
        point.setTotalPoint(initialPoints);
        point.setLastUpdateDate(LocalDateTime.now());

        return pointRepository.save(point);
    }

    @Transactional
    public PointLog earnPoints(UUID pointId, long earnedPoints, String reason) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid point ID"));
        point.setTotalPoint(point.getTotalPoint() + earnedPoints);
        point.setLastUpdateDate(LocalDateTime.now());

        PointLog pointLog = new PointLog();
        pointLog.setPoint(point);
        pointLog.setTransactionType("적립");
        pointLog.setReason(reason);
        pointLog.setAmount(earnedPoints);
        pointLog.setCreatedAt(LocalDateTime.now());

        pointRepository.save(point);
        return pointLogRepository.save(pointLog);
    }

    @Transactional
    public PointLog usePoints(UUID pointId, long points, String reason) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid point ID"));
        if (point.getTotalPoint() < points) {
            throw new IllegalArgumentException("Insufficient points");
        }
        point.setTotalPoint(point.getTotalPoint() - points);
        point.setLastUpdateDate(LocalDateTime.now());

        PointLog pointLog = new PointLog();
        pointLog.setPoint(point);
        pointLog.setTransactionType("사용");
        pointLog.setAmount(points);
        pointLog.setReason(reason);
        pointLog.setCreatedAt(LocalDateTime.now());

        pointRepository.save(point);
        return pointLogRepository.save(pointLog);
    }

    public List<PointLog> getAllPointLogs() {
        return pointLogRepository.findAll();
    }

    public long getTotalPoints(UUID pointId) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid point ID"));
        return point.getTotalPoint();
    }

    public Point getPointById(UUID pointId) {
        return pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid point ID"));
    }
}
