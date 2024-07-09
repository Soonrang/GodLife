package com.example.rewardservice.user.service;

import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final long INITIAL_POINT = 0;

    public User registerUser(String userId, String password, String userName,String userEmail) {
        User user = User.builder()
                .userId(userId)
                .userPassword(passwordEncoder.encode(password))
                .userName(userName)
                .userEmail(userEmail)
                .totalPoint(INITIAL_POINT)
                .lastUpdateDate(LocalDateTime.now())
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findByEmailAndSocial(String email, boolean social) {
        return userRepository.findByUserEmailAndUserSocial(email, social);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
