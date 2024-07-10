package com.example.rewardservice.auth.handler;

import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.dto.UserDTO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 핸들러");

        String email = authentication.getName();
        log.info("Authentication Name: " + email);

        Map<String, Object> claims = Map.of("emailId", email);
        String accessToken = jwtUtil.generateToken(claims, 1);  // 1일
        String refreshToken = jwtUtil.generateToken(claims, 30);  // 30일

        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
        response.getWriter().write(jsonResponse);
    }
}