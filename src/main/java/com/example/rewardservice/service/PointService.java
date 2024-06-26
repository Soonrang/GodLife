package com.example.rewardservice.service;

import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserRepository userRepository;
    private final PointLogRepository pointLogRepository;


    //포인트 획득
    @Transactional
    public PointLogDTO earnPoints(UUID userId, long earnedPoints, String transactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userID"));
        user.setTotalPoint(user.getTotalPoint() + earnedPoints);
        user.setLastUpdateDate(LocalDateTime.now());

        PointLog pointLog = new PointLog();
        pointLog.setUser(user);
        pointLog.setTransactionType(transactionType);
        pointLog.setPointChange(earnedPoints);
        pointLog.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        pointLog = pointLogRepository.save(pointLog);

        return new PointLogDTO(pointLog.getId(), user.getId(), pointLog.getTransactionType(), pointLog.getPointChange(), pointLog.getDescription(), pointLog.getCreatedAt());
    }

    //포인트 사용
    @Transactional
    public PointLogDTO usePoints(UUID userId, long points, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        if (user.getTotalPoint() < points) {
            throw new IllegalArgumentException("Insufficient points");
        }
        user.setTotalPoint(user.getTotalPoint() - points);
        user.setLastUpdateDate(LocalDateTime.now());

        PointLog pointLog = new PointLog();
        pointLog.setUser(user);
        pointLog.setTransactionType("사용");
        pointLog.setPointChange(points);
        pointLog.setDescription(description);
        pointLog.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        pointLog = pointLogRepository.save(pointLog);

        return new PointLogDTO(pointLog.getId(), user.getId(), pointLog.getTransactionType(), pointLog.getPointChange(), pointLog.getDescription(), pointLog.getCreatedAt());
    }
}
