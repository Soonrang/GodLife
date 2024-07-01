package com.example.rewardservice.service;

import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserRepository userRepository;
    private final PointLogRepository pointLogRepository;

    @Transactional
    public PointLogDTO earnPoints(UUID userId, long earnedPoints, String transactionType) {
    User user = findUserById(userId);
    user.earnPoints(earnedPoints);

    PointLog pointLog = dtoToEntity(user, earnedPoints,transactionType, null);
    userRepository.save(user);
    pointLog = pointLogRepository.save(pointLog);

    return entityToDto(pointLog);
    }

    @Transactional
    public PointLogDTO  usePoints(UUID userId, long points, String description) {
        User user = findUserById(userId);
        user.usePoints(points);

        PointLog pointLog = dtoToEntity(user, -points, "사용", description);
        userRepository.save(user);
        pointLog = pointLogRepository.save(pointLog);
        return entityToDto(pointLog);
    }

    @Transactional
    public List<PointLogDTO> getPointLogsByUserId(UUID userId) {
        findUserById(userId);
        List<PointLog> pointLogs = pointLogRepository.findByUserId(userId);
        return pointLogs.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("Invalid userId"));
    }

   private PointLog dtoToEntity(User user,long pointChange, String transactionType, String description) {
        return PointLog.builder()
                .user(user)
                .transactionType(transactionType)
                .pointChange(pointChange)
                .description(description)
                .build();
   }

   private PointLogDTO entityToDto(PointLog pointLog) {
        return PointLogDTO.builder()
                .id(pointLog.getId())
                .userId(pointLog.getUser().getId())
                .transactionType(pointLog.getTransactionType())
                .pointChange(pointLog.getPointChange())
                .description(pointLog.getDescription())
                .createdAt(pointLog.getCreatedAt())
                .build();
   }
}






