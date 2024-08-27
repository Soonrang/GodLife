package com.example.rewardservice.user.presentation;

import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.request.MyPageRequest;
import com.example.rewardservice.user.application.response.MyPageResponse;
import com.example.rewardservice.point.application.PointRecordResponse;
import com.example.rewardservice.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/user")
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

    @PostMapping("/update")
    public ResponseEntity<Void> updateUserInfo(@RequestParam String nickname, @RequestParam(required = false) MultipartFile profileImage) {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MyPageRequest myPageRequest = new MyPageRequest(nickname, profileImage);
        log.info("Received update request: nickname={}, profileImage={}", nickname, profileImage);
        userService.updateUserInfo(email, myPageRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/record")
    public ResponseEntity<List<PointRecordResponse>> getPointRecord() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointRecordResponse> records = userService.getAllPointTransactions(email);
        return ResponseEntity.ok(records);
    }

}
