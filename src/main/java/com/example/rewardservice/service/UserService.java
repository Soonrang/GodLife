package com.example.rewardservice.service;

import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.User.UserDTO;
import com.example.rewardservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Transactional
    public UserDTO viewUser(UUID id) {
        userRepository.findByUserId(id);

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
