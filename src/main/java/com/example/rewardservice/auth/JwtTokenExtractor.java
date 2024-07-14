package com.example.rewardservice.auth;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenExtractor {

    private static final String PREFIX_BEARER = "Bearer ";
    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    public String extractBearerToken(HttpServletRequest request) {
        final String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if(StringUtils.hasText(accessToken)&&accessToken.startsWith(PREFIX_BEARER)){
            return accessToken.substring(PREFIX_BEARER.length());
        }
        final String logMessage = "인증실패 - 엑세스 토큰 추출 실패 : " + accessToken;
        throw new RuntimeException(logMessage);
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
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
}
