package com.example.rewardservice.user.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class MyPageRequest {

    @NotBlank(message = "이름 공백이 될 수 없습니다.")
    @Size(max=15, message = "닉네임은 15자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "닉네임은 공백이 될 수 없습니다.")
    @Size(max=15, message = "닉네임은 15자를 초과할 수 없습니다.")
    private String nickname;

    @Nullable
    private MultipartFile profileImage;



//    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
//    private String password;

    public MyPageRequest(String nickname, MultipartFile profileImage){
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
