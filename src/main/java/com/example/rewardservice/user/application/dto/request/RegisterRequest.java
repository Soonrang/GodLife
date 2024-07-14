package com.example.rewardservice.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterRequest {
    private String email;
    private String password;

    @NotBlank(message = "이름 공백이 될 수 없습니다.")
    @Size(max=15, message = "닉네임은 15자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "닉네임은 공백이 될 수 없습니다.")
    @Size(max=15, message = "닉네임은 15자를 초과할 수 없습니다.")
    private String nickname;

    private MultipartFile imageFile;
}
