//package com.example.rewardservice.security.jwt.filter;
//
//import com.example.rewardservice.security.jwt.util.JWTUtil;
//import com.example.rewardservice.user.domain.MemberState;
//import com.example.rewardservice.user.domain.User;
//import com.example.rewardservice.user.domain.repository.UserRepository;
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.util.Map;
//
//@Log4j2
//public class OauthLoginFilter extends AbstractAuthenticationProcessingFilter {
//
//    private final UserRepository userRepository;
//    private JWTUtil jwtUtil;
//
//    public OauthLoginFilter(String defaultFilterProcessesUrl, UserRepository userRepository) {
//        super(defaultFilterProcessesUrl);
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//
//        log.info("-------------소셜로그인 필터-------------");
//
//        if(request.getMethod().equalsIgnoreCase("GET")){
//            log.info("GET METHOD NOT SUPPORTED");
//            return null;
//        }
//
//        Map<String, String> jsonData = parseRequestJSON(request);
//        log.info(jsonData);
//
//        String idStr = String.valueOf(jsonData.get("id"));
//        String name = (String) jsonData.get("name");
//        String nickname = (String) jsonData.get("nickname");
//        String profileImage = jsonData.get("profileImage");
//
//        User user = userRepository.findByEmail(idStr)
//                .orElseGet(() -> {
//                    User kakaoUser = User.builder()
//                            .email(idStr)
//                            .name(name)
//                            .password(null)
//                            .nickname(nickname)
//                            .userSocial(true)
//                            .totalPoint(0)
//                            .memberState(MemberState.ACTIVE)
//                            .profileImage(profileImage)
//                            .build();
//
//                    userRepository.save(kakaoUser);
//                    return kakaoUser;
//                });
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(user.getEmail(), null);
//
//        return getAuthenticationManager().authenticate(authenticationToken);
//
//    }
//
//    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
//        try(Reader reader = new InputStreamReader(request.getInputStream())) {
//            Gson gson = new Gson();
//            return gson.fromJson(reader, Map.class);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return null;
//    }
//
//}