package com.example.rewardservice.security.oAuth;

import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
//
//    public KakaoResponse signInKakao(KakaoInfo kakaoInfo) {
//
//        String email = kakaoInfo.getId();
//        // 사용자가 존재하지 않을 경우 회원 가입 처리
//        if (!userRepository.existsByEmail(email)) {
//            User user = User.builder()
//                    .userSocial(true)
//                    .name(kakaoInfo.getNickname())
//                    .password(null) // 소셜 로그인 사용자는 비밀번호가 필요하지 않음
//                    .nickname(kakaoInfo.getNickname())
//                    .totalPoint(0)
//                    .email(kakaoInfo.getId())
//                    .id(UUID.fromString(kakaoInfo.getId()))
//                    .build();
//
//            userRepository.save(user);
//            KakaoResponse
//        }
//        return KakaoResponse;
//    }
}

