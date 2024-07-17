package com.example.rewardservice.user.application.dto.request;

import com.example.rewardservice.user.domain.MemberState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Data
@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    //@NotBlank(message = "비밀번호는 필수 입력값입니다.")
    //@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
    //        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "이름 공백이 될 수 없습니다.")
    @Size(max=15, message = "닉네임은 15자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String nickname;

    private MultipartFile imageFile;

}
