package com.example.rewardservice.auth.config;

import com.example.rewardservice.auth.filter.TokenCheckFilter;
import com.example.rewardservice.auth.filter.UserLoginFilter;
import com.example.rewardservice.auth.handler.UserLoginSuccessHandler;
import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final UserDetailService userDetailService;
    private final JWTUtil jwtUtil;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("----------web configure---------");

        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        log.info("------configure-----");
        AuthenticationManagerBuilder  authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        UserLoginFilter userLoginFilter = new UserLoginFilter("/generateToken");
        userLoginFilter.setAuthenticationManager(authenticationManager);


        UserLoginSuccessHandler successHandler = new UserLoginSuccessHandler(jwtUtil);
        userLoginFilter.setAuthenticationSuccessHandler(successHandler);

        http.addFilterBefore(userLoginFilter, UsernamePasswordAuthenticationFilter.class);


        //refreshToken 호출 처리
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil, UserDetailService),
                UsernamePasswordAuthenticationFilter.class
        );
        //http.csrf().disable();

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        return http.build();
    }
}
