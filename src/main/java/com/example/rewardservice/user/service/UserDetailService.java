package com.example.rewardservice.user.service;

import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.dto.UserDTO;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findByUserId(userId); // userId를 기준으로 검색

        User user = result.orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다."));

        log.info("UserDetailService user: " + user);

        // UserDTO 객체 생성
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUserId(),
                user.getUserPassword(),
                user.getUserName(),
                user.getUserEmail(),
                user.getTotalPoint(),
                user.getLastUpdateDate(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        log.info(userDTO);

        return userDTO;
    }
}
