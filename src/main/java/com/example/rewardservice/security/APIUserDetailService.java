package com.example.rewardservice.security;

import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.security.jwt.dto.APIUserDetailDto;
import com.example.rewardservice.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findByEmail(email); // email를 기준으로 검색
        log.info("Load User By Email: " + email);
        User user = result.orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다."));

        log.info("UserDetailService user: " + user.toString());

        // 기본적으로 모든 사용자에게 ROLE_USER 권한 부여
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // 특정 이메일에 대해 추가 권한 부여
        if ("admin@gmail.com".equals(user.getEmail())) {
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        }

        // UserDTO 객체 생성
        APIUserDetailDto APIUserDetailDTO = new APIUserDetailDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getNickname(),
                user.getTotalPoint(),
                user.getProfileImage(),
                authorities
        );

        log.info(APIUserDetailDTO);

        return APIUserDetailDTO;
    }
}
