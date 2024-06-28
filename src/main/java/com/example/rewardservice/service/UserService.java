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

    //임시로 유저 포인트를 생성
    @Transactional
    public UserDTO createUser(String userId, long initialPoints) {
        User user = User.builder()
                .userId(userId)
                .totalPoint(initialPoints)
                .lastUpdateDate(LocalDateTime.now())
                .build();

        user = userRepository.save(user);
        return entityToDto(user);
    }

    private UserDTO entityToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .totalPoint(user.getTotalPoint())
                .lastUpdateDate(user.getLastUpdateDate())
                .build();
    }

}
