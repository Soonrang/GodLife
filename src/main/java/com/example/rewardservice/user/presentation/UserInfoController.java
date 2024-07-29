package com.example.rewardservice.user.presentation;

import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.dto.request.MyPageRequest;
import com.example.rewardservice.user.application.dto.response.MyPageResponse;
import com.example.rewardservice.user.application.UserService;
import com.example.rewardservice.user.application.dto.response.PointHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {
    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);
    private final UserService userService;
    private final JwtTokenExtractor jwtTokenExtractor;

    @GetMapping
    public ResponseEntity<MyPageResponse> getUserInfo()  {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MyPageResponse userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfo);
    }


    @GetMapping("/points-history")
    public ResponseEntity<List<PointHistoryResponse>> getUserPointHistory() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointHistoryResponse> pointHistory = userService.getUserPointHistory(email);
        return ResponseEntity.ok(pointHistory);
    }


    @PutMapping("/update")
    public ResponseEntity<MyPageResponse> updateUserInfo(@ModelAttribute MyPageRequest myPageRequest) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MyPageResponse updatedUserInfo = userService.updateUserInfo(email, myPageRequest);
        return ResponseEntity.ok(updatedUserInfo);
    }

}
