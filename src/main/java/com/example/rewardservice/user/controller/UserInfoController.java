package com.example.rewardservice.user.controller;


import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.dto.response.MyPageResponse;
import com.example.rewardservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MyPageResponse userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfo);
    }

}
