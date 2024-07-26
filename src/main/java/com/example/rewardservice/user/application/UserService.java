package com.example.rewardservice.user.application;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import com.example.rewardservice.image.application.service.ProfileImageService;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.user.application.dto.response.PointHistoryResponse;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.application.dto.request.MyPageRequest;
import com.example.rewardservice.user.application.dto.request.RegisterRequest;
import com.example.rewardservice.user.application.dto.response.MyPageResponse;
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
    private final ProfileImageService profileImageService;
    private static final long INITIAL_POINT = 0;
    private final PointRepository pointRepository;

    //RegisterRequest 수정 필요
    public User registerUser(RegisterRequest registerRequest) {
        StoreImageDto storeImageDto = profileImageService.storeProfileImage(registerRequest.getProfileImage());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .nickname(registerRequest.getNickname())
                .totalPoint(INITIAL_POINT)
                .profileImage(storeImageDto.getStoreName())
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public MyPageResponse getUserInfo(final String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("아이디 없음"));
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

    @Transactional
    public MyPageResponse updateProfileImage(String email, MyPageRequest myPageRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));

        StoreImageDto storeImageDto = null;
        if (myPageRequest.getProfileImage() != null && !myPageRequest.getProfileImage().isEmpty()) {
            storeImageDto = profileImageService.storeProfileImage(myPageRequest.getProfileImage());
            String imageUrl = storeImageDto.getStoreName();
            user.setProfileImage(imageUrl);
        }

        userRepository.save(user);

        return new MyPageResponse(user.getEmail(), user.getNickname(), user.getName(), user.getTotalPoint(),user.getProfileImage());

    }

    public List<PointHistoryResponse> getUserPointHistory(String email) {
        return pointRepository.findByUserEmail(email).stream()
                .map(point -> new PointHistoryResponse(
                        point.getPointType(),
                        point.getPointChange(),
                        point.getDescription(),
                        point.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }


    //빌더패턴 적용
   /* @Transactional
    public MyPageResponse updateUserInfo(String email, MyPageRequest myPageRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));

        StoreImageDto storeImageDto = null;
        if (myPageRequest.getProfileImage() != null && !myPageRequest.getProfileImage().isEmpty()) {
            storeImageDto = profileImageService.storeProfileprofileImage(myPageRequest.getProfileImage());
            String imageUrl = storeImageDto.getStoreName();
            user.setprofileImage(imageUrl);
        }

        if (myPageRequest.getName() != null) {
            user.setName(myPageRequest.getName());
        }
        if (myPageRequest.getNickname() != null) {
            user.setNickname(myPageRequest.getNickname());
        }

        userRepository.save(user);

        return new MyPageResponse(user.getEmail(), user.getNickname(), user.getName(), user.getTotalPoint(),user.getProfileImage());
    }
*/

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nickNameExists(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

}