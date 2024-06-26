package com.example.rewardservice.service;

import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.dto.User.UserDTO;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PointLogRepository pointLogRepository;

    //임시로 유저 포인트를 생성
    @Transactional
    public UserDTO createUser(String userId, long initialPoints) {
        User user = new User();
        user.setUserId(userId);
        user.setTotalPoint(initialPoints);
        user.setLastUpdateDate(LocalDateTime.now());

        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getUserId(), user.getTotalPoint(), user.getLastUpdateDate());
    }



    //특정 유저로 포인트로그 목록 조회
    public List<PointLogDTO> getPointLogsByUserId(UUID userId) {
        List<PointLog> pointLogs = pointLogRepository.findByUserId(userId);
        return pointLogs.stream()
                .map(log -> new PointLogDTO(log.getId(), log.getUser().getId(), log.getTransactionType(), log.getPointChange(), log.getDescription(), log.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
