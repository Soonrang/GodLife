package com.example.rewardservice.user.presentation;

import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.security.jwt.JwtTokenExtractor;
import com.example.rewardservice.user.application.request.RegisterRequest;
import com.example.rewardservice.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final S3ImageService s3ImageService;

    //폼데이터와 파일 업로드를 함께 처리할 때에는 @ModelAttribute 편리하다.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@ModelAttribute RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("유저가 성공적으로 등록되었습니다.");
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<?> s3Upload(@RequestPart(value ="image", required = false)MultipartFile image) {
        String profileImage = s3ImageService.upload(image);
        return ResponseEntity.ok(profileImage);
    }

    @GetMapping("/s3/delete")
    public ResponseEntity<?> s3delete(@RequestParam String url){
        s3ImageService.deleteImageFromS3(url);
        return ResponseEntity.ok(null);
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAccount() {
        String email = jwtTokenExtractor.getCurrentUserEmail();
        userService.deleteAccount(email);
        return ResponseEntity.ok("Account deleted successfully");
    }

}
