package com.example.rewardservice.user.controller;

import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.dto.request.LoginRequest;
import com.example.rewardservice.user.dto.request.MyPageRequest;
import com.example.rewardservice.user.dto.response.LoginResponse;
import com.example.rewardservice.user.dto.request.RegisterRequest;
import com.example.rewardservice.user.service.APIUserDetailService;
import com.example.rewardservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final APIUserDetailService APIUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("name") String name,
                                          @RequestParam("nickname") String nickname,
                                          @RequestParam("profileUrl") String profileUrl) {
        userService.registerUser(email, password, name, nickname, profileUrl);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(Map.of("email", exists));
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        boolean exists = userService.nickNameExists(nickname);
        return ResponseEntity.ok(Map.of("nickname", exists));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = APIUserDetailService.loadUserByUsername(loginRequest.getEmail());
        if (userDetails == null || !passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("name", userDetails.getUsername());

        String accessToken = jwtUtil.generateToken(claims, 1); // 1 day expiration
        String refreshToken = jwtUtil.generateToken(claims, 30); // 30 days expiration

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<String> deleteAccount(@PathVariable String email) {
        userService.deleteAccount(email);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @PutMapping("/users/{email}")
    public ResponseEntity<String> updateUserInfo(@PathVariable String email,
                                                 @RequestPart("myPageRequest") MyPageRequest myPageRequest) {
        userService.updateUserInfo(email,
                myPageRequest.getPassword(),
                myPageRequest.getProfileImageUrl(),
                myPageRequest.getNickname(),
                myPageRequest.getName());
        return ResponseEntity.ok("User info updated successfully");
    }
}
