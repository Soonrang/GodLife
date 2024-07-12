package com.example.rewardservice.user.service;

import com.example.rewardservice.Image.dto.StoreImageDto;
import com.example.rewardservice.Image.service.ImageFileService;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.dto.response.MyPageResponse;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileService imageFileService;
    private static final long INITIAL_POINT = 0;


    //RegisterRequest로 수정 필요
    public User registerUser(String email, String password, String userName,
                             String nickname, MultipartFile profileImage) {

        StoreImageDto storeImageDto = imageFileService.storeImageFile(profileImage);
        String profileImageUrl = storeImageDto.getStoreName();

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(userName)
                .nickname(nickname)
                .totalPoint(INITIAL_POINT)
                .profileImageUrl(profileImageUrl)
                .lastUpdateDate(LocalDateTime.now())
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public MyPageResponse getUserInfo(final String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("아이디가 없음"));
        return MyPageResponse.from(user);
    }

    @Transactional
    public void deleteAccount(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();
        user.setMemberState(MemberState.DELETED); // 회원 상태를 비활성화로 변경
        userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nickNameExists(String nickName) {
        return userRepository.existsByNickname(nickName);
    }


    @Transactional
    public User updateUserInfo(String email, String newPassword, MultipartFile newProfileImage,
                               String newNickName, String newName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();
        if (newPassword != null) {
            user.changePassword(newPassword);
        }
        if (newProfileImage != null && !newProfileImage.isEmpty()) {
            StoreImageDto storeImageDto = imageFileService.storeImageFile(newProfileImage);
            user.setProfileImageUrl(storeImageDto.getStoreName());
        }
        if (newNickName != null) {
            user.setNickname(newNickName);
        }
        if (newName != null) {
            user.setName(newName);
        }
        user.setLastUpdateDate(LocalDateTime.now());

        return userRepository.save(user);
    }
}