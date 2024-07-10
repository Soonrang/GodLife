package com.example.rewardservice.user.service;

import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final long INITIAL_POINT = 0;

    public User registerUser(String email, String password, String userName) {
        User user = User.builder()
                .password(passwordEncoder.encode(password))
                .name(userName)
                .email(email)
                .totalPoint(INITIAL_POINT)
                .lastUpdateDate(LocalDateTime.now())
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByUserId(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public User save(User user) {
        return userRepository.save(user);
    }
}
