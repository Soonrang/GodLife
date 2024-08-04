package com.example.rewardservice.user.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.image.application.dto.ImageDto;
import com.example.rewardservice.image.application.service.ImageService;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.shop.domain.repository.PurchaseRecordRepository;
import com.example.rewardservice.user.application.response.PointHistoryResponse;
import com.example.rewardservice.user.application.response.PointRecordResponse;
import com.example.rewardservice.user.domain.GiftRecord;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.application.request.MyPageRequest;
import com.example.rewardservice.user.application.request.RegisterRequest;
import com.example.rewardservice.user.application.response.MyPageResponse;
import com.example.rewardservice.user.domain.repository.GiftRecordRepository;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
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
    public MyPageResponse updateUserInfo(String email, MyPageRequest myPageRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));
        String newNickname = myPageRequest.getNickname();

        MultipartFile profileImage = myPageRequest.getProfileImage();
        if (profileImage == null || profileImage.isEmpty()) {
            throw new IllegalArgumentException("프로필 사진이 없습니다.");
        }

        ImageDto ImageDto = imageService.saveImage(myPageRequest.getProfileImage());
        String newProfileImage = ImageDto.getStoreName();

        user.updateUserInfo(newNickname,newProfileImage);
        userRepository.save(user);

        return new MyPageResponse(user.getEmail(), user.getNickname(), user.getName(), user.getTotalPoint(),user.getProfileImage());
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