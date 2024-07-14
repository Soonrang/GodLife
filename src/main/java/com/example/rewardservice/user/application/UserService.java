package com.example.rewardservice.user.application;

import com.example.rewardservice.Image.application.dto.StoreImageDto;
import com.example.rewardservice.Image.application.service.ImageFileService;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.application.dto.request.MyPageRequest;
import com.example.rewardservice.user.application.dto.request.RegisterRequest;
import com.example.rewardservice.user.application.dto.response.MyPageResponse;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileService imageFileService;
    private static final long INITIAL_POINT = 0;

    //RegisterRequest 수정 필요
    public User registerUser(RegisterRequest registerRequest) {

        StoreImageDto storeImageDto = imageFileService.storeImageFile(registerRequest.getImageFile());
        String ImageFile = storeImageDto.getStoreName();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .nickname(registerRequest.getNickname())
                .totalPoint(INITIAL_POINT)
                .ImageFile(ImageFile)
                .userSocial(false)
                .memberState(MemberState.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public MyPageResponse getUserInfo(final String email) throws IOException {
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
        if (myPageRequest.getImageFile() != null && !myPageRequest.getImageFile().isEmpty()) {
            storeImageDto = imageFileService.storeProfileImageFile(myPageRequest.getImageFile());
            String imageUrl = storeImageDto.getStoreName();
            user.setImageFile(imageUrl);
        }

        userRepository.save(user);

        return new MyPageResponse(user.getEmail(), user.getNickname(), user.getName(), user.getTotalPoint(),user.getImageFile());

    }

    //빌더패턴 적용
   /* @Transactional
    public MyPageResponse updateUserInfo(String email, MyPageRequest myPageRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));

        StoreImageDto storeImageDto = null;
        if (myPageRequest.getImageFile() != null && !myPageRequest.getImageFile().isEmpty()) {
            storeImageDto = imageFileService.storeProfileImageFile(myPageRequest.getImageFile());
            String imageUrl = storeImageDto.getStoreName();
            user.setImageFile(imageUrl);
        }

        if (myPageRequest.getName() != null) {
            user.setName(myPageRequest.getName());
        }
        if (myPageRequest.getNickname() != null) {
            user.setNickname(myPageRequest.getNickname());
        }

        userRepository.save(user);

        return new MyPageResponse(user.getEmail(), user.getNickname(), user.getName(), user.getTotalPoint(),user.getImageFile());
    }
*/

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nickNameExists(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

}