package com.example.rewardservice.service;

import com.example.rewardservice.domain.Point.Point;
import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.dto.Point.PointDTO;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    //임시로 유저 포인트를 생성
    @Transactional
    public PointDTO createPoint(String userId, long initialPoints) {
        Point point = new Point();
        point.setUserId(userId);
        point.setTotalPoint(initialPoints);
        point.setLastUpdateDate(LocalDateTime.now());

        point = pointRepository.save(point);
        return new PointDTO(point.getId(), point.getUserId(), point.getTotalPoint(), point.getLastUpdateDate());
    }

    //포인트 획득
    @Transactional
    public PointLogDTO earnPoints(UUID pointId, long earnedPoints, String transactionType) {
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid point ID"));
        point.setTotalPoint(point.getTotalPoint() + earnedPoints);
        point.setLastUpdateDate(LocalDateTime.now());

        PointLog pointLog = new PointLog();
        pointLog.setPoint(point);
        pointLog.setTransactionType(transactionType);
        pointLog.setPointChange(earnedPoints);
        pointLog.setCreatedAt(LocalDateTime.now());

        pointRepository.save(point);
        pointLog = pointLogRepository.save(pointLog);

        return new PointLogDTO(pointLog.getId(), point.getId(), pointLog.getTransactionType(), pointLog.getPointChange(), pointLog.getDescription(), pointLog.getCreatedAt());
    }

    //포인트 사용
    @Transactional
    public PointLogDTO usePoints(UUID pointId, long points, String description) {
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
        pointLog.setPointChange(points);
        pointLog.setDescription(description);
        pointLog.setCreatedAt(LocalDateTime.now());

        pointRepository.save(point);
        pointLog = pointLogRepository.save(pointLog);

        return new PointLogDTO(pointLog.getId(), point.getId(), pointLog.getTransactionType(), pointLog.getPointChange(), pointLog.getDescription(), pointLog.getCreatedAt());
    }

    //특정 유저로 포인트로그 목록 조회
    public List<PointLogDTO> getPointLogsByUserId(UUID userId) {
        List<PointLog> pointLogs = pointLogRepository.findByUserId(userId);
        return pointLogs.stream()
                .map(log -> new PointLogDTO(log.getId(), log.getPoint().getId(), log.getTransactionType(), log.getPointChange(), log.getDescription(), log.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
