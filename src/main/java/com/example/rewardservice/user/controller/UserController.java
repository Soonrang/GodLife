package com.example.rewardservice.user.controller;

import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.dto.request.LoginRequest;
import com.example.rewardservice.user.dto.response.LoginResponse;
import com.example.rewardservice.user.dto.request.RegisterRequest;
import com.example.rewardservice.user.service.APIUserDetailService;
import com.example.rewardservice.user.service.UserManagementService;
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

    private final UserManagementService userManagementService;
    private final JWTUtil jwtUtil;
    private final APIUserDetailService APIUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        userManagementService.registerUser(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getName(),
                registerRequest.getNickName(), registerRequest.getProfileUrl());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = userManagementService.emailExists(email);
        return ResponseEntity.ok(Map.of("email", exists));
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

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteUser() {
        return null;
    }
}