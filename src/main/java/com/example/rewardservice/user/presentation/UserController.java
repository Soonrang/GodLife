package com.example.rewardservice.user.presentation;

import com.example.rewardservice.auth.util.JWTUtil;
import com.example.rewardservice.user.application.dto.request.LoginRequest;
import com.example.rewardservice.user.application.dto.request.RegisterRequest;
import com.example.rewardservice.user.application.dto.response.LoginResponse;
import com.example.rewardservice.user.application.APIUserDetailService;
import com.example.rewardservice.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final APIUserDetailService APIUserDetailService;
    private final PasswordEncoder passwordEncoder;

    //폼데이터와 파일 업로드를 함께 처리할 때에는 @ModelAttribute 편리하다.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@ModelAttribute RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("유저가 성공적으로 등록되었습니다.");
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

        // 인증 성공 시, Access Token과 Refresh Token 발급
        String accessToken = jwtUtil.generateToken(Map.of("email", userDetails.getUsername()), 1); // 1일 유효기간
        String refreshToken = jwtUtil.generateToken(Map.of("email", userDetails.getUsername()), 30); // 30일 유효기간

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

//    @DeleteMapping("/users/{email}")
//    public ResponseEntity<String> deleteAccount(@PathVariable String email) {
//        userService.deleteAccount(email);
//        return ResponseEntity.ok("Account deleted successfully");
//    }

//    @PutMapping("/users/{email}")
//    public ResponseEntity<String> updateUserInfo(@PathVariable String email,
//                                                 @RequestPart("myPageRequest") MyPageRequest myPageRequest) {
//        userService.updateUserInfo(email,
//                myPageRequest.getPassword(),
//                myPageRequest.getImageFile(),
//                myPageRequest.getNickname(),
//                myPageRequest.getName());
//        return ResponseEntity.ok("User info updated successfully");
//    }
//@PostMapping("/upload/single")
//public StoreImageDto uploadSingleImage(@RequestParam("file")MultipartFile file){
//    return imageFileService.storeImageFile(file);
//}


}
