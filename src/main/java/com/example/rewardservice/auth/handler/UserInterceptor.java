package com.example.rewardservice.auth.handler;

import com.example.rewardservice.auth.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInterceptor implements HandlerInterceptor {

    private final JwtToken
    private final JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken =

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
