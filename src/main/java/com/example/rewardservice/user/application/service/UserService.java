package com.example.rewardservice.user.application.service;


import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.user.application.response.PointRecordResponse;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.application.request.MyPageRequest;
import com.example.rewardservice.user.application.request.RegisterRequest;
import com.example.rewardservice.user.application.response.MyPageResponse;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;
    private final S3ImageService s3ImageService;
    private static final long INITIAL_POINT = 0;

    //RegisterRequest 수정 필요
    public void registerUser(RegisterRequest registerRequest) {
        String profileImageUrl = s3ImageService.upload(registerRequest.getProfileImage());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .nickname(registerRequest.getNickname())
                .totalPoint(INITIAL_POINT)
                .profileImage(profileImageUrl)
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        userRepository.save(user);
    }

    public MyPageResponse getUserInfo(final String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("아이디 없음"));
        return MyPageResponse.from(user);
    }

    public List<PointRecordResponse> getAllPointTransactions(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("아이디 없음"));

        return pointRepository.findByUser(user)
                .stream()
                .map(PointRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<PointRecordResponse> getPointTransactionsByType(String email, String type) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("아이디 없음"));

        return pointRepository.findByUserAndType(user, type)
                .stream()
                .map(PointRecordResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserInfo(String email, MyPageRequest myPageRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));
        String newNickname = myPageRequest.getNickname();

        String nowProfileImage = user.getProfileImage();

        if(myPageRequest.getProfileImage()!=null) {
            nowProfileImage = s3ImageService.upload(myPageRequest.getProfileImage());
            s3ImageService.deleteImageFromS3(user.getProfileImage());
            user.updateUserInfo(newNickname, nowProfileImage);
        }

        user.updateUserInfo(newNickname,nowProfileImage);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();
        user.changeMemberState(MemberState.DELETED); // 회원 상태를 비활성화로 변경
        userRepository.save(user);
    }


    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nickNameExists(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

}