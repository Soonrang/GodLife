//package com.example.rewardservice.security.jwt.config;
//
//import com.example.rewardservice.user.domain.User;
//import com.example.rewardservice.user.domain.repository.UserRepository;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SocialAuthenticationProvider implements AuthenticationProvider {
//
//    private final UserRepository userRepository;
//
//    public SocialAuthenticationProvider(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        if(!(authentication instanceof UsernamePasswordAuthenticationToken)) {
//            return null;
//        }
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
//
//        return new UsernamePasswordAuthenticationToken(user.getEmail(), null);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
