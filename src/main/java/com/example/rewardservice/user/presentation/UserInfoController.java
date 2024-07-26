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
        log.info("***********************************************"+email);
        MyPageResponse userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/updateImage")
    public ResponseEntity<MyPageResponse> updateImage(@ModelAttribute MyPageRequest myPageRequest) throws IOException {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MyPageResponse updateUserInfo = userService.updateProfileImage(email,myPageRequest);
        return ResponseEntity.ok(updateUserInfo);
    }

    @GetMapping("/points-history")
    public ResponseEntity<List<PointHistoryResponse>> getUserPointHistory() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        List<PointHistoryResponse> pointHistory = userService.getUserPointHistory(email);
        return ResponseEntity.ok(pointHistory);
    }


   /* @PutMapping("/update")
    public ResponseEntity<MyPageResponse> updateUserInfo(@ModelAttribute MyPageRequest myPageRequest) throws IOException {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        MyPageResponse updatedUserInfo = userService.updateUserInfo(email, myPageRequest);
        return ResponseEntity.ok(updatedUserInfo);
    }
*/
//    @GetMapping
//    public ResponseEntity<MyPageResponse> getUserInfo() throws IOException {
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//        MyPageResponse userInfo = userService.getUserInfo(email);
//        return ResponseEntity.ok(userInfo);
//    }
//
//    @GetMapping("/profileImage")
//    public ResponseEntity<Resource> getUserProfileImage() throws IOException {
//        String email = jwtTokenExtractor.getCurrentUserEmail();
//        log.info("Extracted email: {}", email);
//
//        MyPageResponse userInfo = userService.getUserInfo(email);
//        log.info("User info retrieved: {}", userInfo);
//
//        String profileImageUrl = userInfo.getProfileImage();
//        log.info("Profile image URL: {}", profileImageUrl);
//
//        byte[] imageBytes = profileImageService.getProfileImage(profileImageUrl);
//        log.info("Image bytes length: {}", imageBytes.length);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG); // 이미지의 MIME 타입을 적절히 설정
//        headers.setContentLength(imageBytes.length);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(new ByteArrayResource(imageBytes));
//    }

}
