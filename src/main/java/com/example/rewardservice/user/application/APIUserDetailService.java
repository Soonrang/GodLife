package com.example.rewardservice.user.application;

import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.application.dto.APIUserDetailDto;
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

        List<GrantedAuthority> authorities;
        if ("admin@gmail.com".equals(user.getEmail())) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
