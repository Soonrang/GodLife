package com.example.rewardservice.security.jwt.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenExtractor {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenExtractor.class);

    private static final String PREFIX_BEARER = "Bearer ";
    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    private final HttpServletRequest request;

    public String getCurrentUserEmail() {
        String bearerToken = request.getHeader("Authorization");
        log.info("***************************Received Token: " + bearerToken);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

/*
    public String extractBearerToken(HttpServletRequest request) {
        final String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if(StringUtils.hasText(accessToken)&&accessToken.startsWith(PREFIX_BEARER)){
            return accessToken.substring(PREFIX_BEARER.length());
        }
        final String logMessage = "인증실패 - 엑세스 토큰 추출 실패 : " + accessToken;
        throw new RuntimeException(logMessage);
    }


    public String getCurrentEmail() {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
//            if (jwtUtil.validateToken(token)) {
//
//            }
        }

        return jwtUtil.extractEmail(token);
    }

    public String getTokenRes(String headerKey, HttpServletResponse response) {
        final String accessToken = response.getHeader(headerKey);
        if(StringUtils.hasText(accessToken)&&accessToken.startsWith(PREFIX_BEARER)){
            return accessToken.substring(PREFIX_BEARER.length());
        }
        final String logMessage = "인증실패 - 엑세스 토큰 추출 실패 : " + accessToken;
        throw new RuntimeException(logMessage);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if(StringUtils.hasText(refreshToken) && refreshToken.startsWith(PREFIX_BEARER)) {
            return refreshToken.substring(PREFIX_BEARER.length());
        }

        final String logMessage = "인증실패 - 리프레시 토큰 추출 실패: " + refreshToken;
        throw new RuntimeException(logMessage);
    }

*/

}
