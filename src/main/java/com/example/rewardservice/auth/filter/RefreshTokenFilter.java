package com.example.rewardservice.auth.filter;

import com.example.rewardservice.auth.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(!path.equals(refreshPath)) {
            log.info("skip refresh token filter");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Refresh token filter");

        //전송된 JSON에서 accessToken과 refreshToken을 얻어온다.
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");

        log.info("accessToken: "+ accessToken);
        log.info("refreshToken: "+ refreshToken);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try(Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch(IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
