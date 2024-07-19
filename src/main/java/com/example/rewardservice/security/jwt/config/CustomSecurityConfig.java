package com.example.rewardservice.security.jwt.config;

import com.example.rewardservice.security.jwt.filter.RefreshTokenFilter;
import com.example.rewardservice.security.jwt.filter.TokenCheckFilter;
import com.example.rewardservice.security.jwt.filter.UserLoginFilter;
import com.example.rewardservice.security.jwt.handler.UserLoginSuccessHandler;
import com.example.rewardservice.security.jwt.util.JWTUtil;
import com.example.rewardservice.user.application.APIUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig{

    private final APIUserDetailService APIUserDetailService;
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

        //AuthenticationManager설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(APIUserDetailService).passwordEncoder(passwordEncoder());

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        //APILoginFilter
        UserLoginFilter userLoginFilter = new UserLoginFilter("/api/login");
        userLoginFilter.setAuthenticationManager(authenticationManager);

        //APILoginSuccessHandler
        UserLoginSuccessHandler successHandler = new UserLoginSuccessHandler(jwtUtil);
        //UserLoginSuccessHandler successHandler = new UserLoginSuccessHandler(jwtUtil,APIUserDetailService);

        //SuccessHandler 세팅
        userLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //APILoginFilter의 위치 조정
        http.addFilterBefore(userLoginFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(
                tokenCheckFilter(jwtUtil, APIUserDetailService), UsernamePasswordAuthenticationFilter.class);

        //refreshToken 호출
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil),TokenCheckFilter.class);


        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/check-email", "/api/register", "/api/login",
                        "/api/logout", "/api/check-nickname",
                        "/user/profileImage", "/event/**", "/user/**","/images/**").permitAll()
                .anyRequest().authenticated()
        );


//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(
//                        "/api/**",
//                        "/user/**",
//                        "/event/**").permitAll()
//                .anyRequest().authenticated()
//        );

        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, APIUserDetailService APIUserDetailService){
        return new TokenCheckFilter(jwtUtil, APIUserDetailService);
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
