package com.example.rewardservice.auth.filter;

import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(!path.startsWith("/api/")){
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token Check Filter");
        log.info("JWTUtil:" + jwtUtil);

        filterChain.doFilter(request,response);
    }

}
