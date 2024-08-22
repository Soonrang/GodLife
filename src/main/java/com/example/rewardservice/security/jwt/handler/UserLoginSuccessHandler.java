package com.example.rewardservice.security.jwt.handler;

import com.example.rewardservice.security.jwt.dto.APIUserDetailDto;
import com.example.rewardservice.security.jwt.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 핸들러");

        String email = authentication.getName();
        log.info("Authentication Name: " + email);

        //APIUserDetailDto userDetails = (APIUserDetailDto) authentication.getPrincipal();
        //UUID userId = userDetails.getId(); // UUID를 직접 가져옵니다.


        Map<String, Object> claims = Map.of("email", email);
        String accessToken = jwtUtil.generateToken(claims, 1);  // 1일
        String refreshToken = jwtUtil.generateToken(claims, 30);  // 30일

        log.info("Access Token: " + accessToken);

        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(Map.of("accessToken", accessToken, "refreshToken", refreshToken,"userId", email ));
        response.getWriter().write(jsonResponse);
    }
}